package ga.backend.customer.service;

import ga.backend.company.entity.Company;
import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.entity.Customer;
import ga.backend.customer.entity.Gender;
import ga.backend.customer.repository.CustomerRepository;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.entity.DataType;
import ga.backend.customerType.service.CustomerTypeService;
import ga.backend.dong2.entity.Dong2;
import ga.backend.dong2.service.Dong2Service;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.gu2.entity.Gu2;
import ga.backend.gu2.service.Gu2Service;
import ga.backend.metro2.entity.Metro2;
import ga.backend.metro2.service.Metro2Service;
import ga.backend.schedule.entity.Schedule;
import ga.backend.customer.entity.ConsultationStatus;
import ga.backend.util.CalculateAge;
import ga.backend.util.FindCoordinateByKakaoMap;
import ga.backend.util.FindEmployee;
import ga.backend.util.InitialCustomerTypeNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerTypeService customerTypeService;
    private final Dong2Service dong2Service;
    private final Gu2Service gu2Service;
    private final Metro2Service metro2Service;

    private final FindEmployee findEmployee;
    private final FindCoordinateByKakaoMap findCoordinateByKakaoMap;

    // CREATE
    public Customer createCustomer(Customer customer, CustomerRequestDto.MetroGuDong metroGuDong) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        customer.setEmployee(employee);

        // liPk를 이용한 dongString 자동 설정
//        if (liPk != 0) {
//            Li li = liService.findLi(liPk);
//            customer.setLi(li);
//            customer.setDongString(liService.findDongString(li));
//        }

        if (customer.getContractYn() == null) customer.setContractYn(false);

        // metro, gu, dong을 이용한 dongString 자동 설정
        makeDongString(metroGuDong, customer);

        // customerType 설정
        long customerTypePk = customer.getCustomerType().getPk();
        CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);
        if(customerType.getCompany() != employee.getCompany()) // 다른 회사의 customertype을 사용하는지 확인
            throw new BusinessLogicException(ExceptionCode.EMPLOYEE_AND_CUSTOMERTYPE_NOT_MATCH);
        customer.setCustomerType(customerType);

        // 만나이 계산
        if(customer.getBirth() != null) customer.setAge(CalculateAge.getAge(customer.getBirth()));

        return customerRepository.save(customer);
    }

    // 여러 명의 custemer 생성
    @Transactional
    public List<Customer> createCustomers(List<Customer> customers) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<CustomerType> customerTypes = customerTypeService.findCustomerTypeByCompanyFromEmployee(employee); // 고객의 customerType

        // NULL 유형의 고객유형
        CustomerType nullCustomerType = customerTypes.stream().filter(c -> c.getName().equals("000")).findFirst().orElse(null);
        if(nullCustomerType == null) nullCustomerType = customerTypeService.createNULLCustomerType(employee);

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            customer.setEmployee(employee);

            if (customer.getContractYn() == null) customer.setContractYn(false);

            // metro, gu, dong을 이용한 dongString 자동 설정
            if (customer.getDongString() != null) {
                makeDongString(customer.getDongString(), customers.get(i));
            }

            // customerType 설정
            String customerTypeName = customer.getCustomerType().getName();
            CustomerType customerType;
            if(customerTypeName == null) customerType = nullCustomerType;
            else {
                customerType = customerTypes.stream()
                        .filter(c -> c.getName().equals(customerTypeName))
                        .findFirst().orElse(null);
                if(customerType == null) { // 없는 고객유형인 경우
//                throw new BusinessLogicException(ExceptionCode.EMPLOYEE_AND_CUSTOMERTYPE_NOT_MATCH, "customerType is null { customer's name : " + customer.getName() + " }");
                    if(Pattern.matches("^[a-zA-Z0-9가-힣]{1,10}$", customerTypeName)) { // 생성가능한 이름인 경우
                        // 새로운 고객유형 생성하기
                        CustomerType newCustomerType = new CustomerType();
                        newCustomerType.setName(customerTypeName);
                        newCustomerType.setDataType(DataType.DB);
                        customerType = customerTypeService.createCustomerType(newCustomerType);
                    } else {
                        // 새로운 고객유형 생성하기 실패하면 default 고객유형으로 하기
                        customerType = nullCustomerType;
                    }
                }
            }
            customer.setCustomerType(customerType);

            // 만나이 계산
            if(customer.getBirth() != null) customer.setAge(CalculateAge.getAge(customer.getBirth()));

            customerRepository.save(customer);
        }

        return customers;
    }


    // READ
    public Customer findCustomer(long customerPk) {
        Customer customer = verifiedCustomer(customerPk);
        return customer;
    }

    // 행정구를 이용한 좌표값 구하기
    public List<Map<String, Double>> findCoordinate(List<Customer> customers) {
        List<Map<String, Double>> list = new ArrayList<>();

        for (Customer customer : customers) {
            Map<String, Double> map = new HashMap<>();

            if (customer.getDong2() != null) { // dong이 있는 경우
                Dong2 dong2 = customer.getDong2();

                if (dong2.getDongName().isEmpty()) { // dong 이름이 없는 경우 삭제하기
                    dong2Service.deleteDong(dong2);
                } else if (dong2.getX() == null) { // 좌표값이 없는 경우 -> 구하기
                    map = findCoordinateByKakaoMap.findCoordinate(dong2.getDongName());
                    dong2.setX(map.get("x"));
                    dong2.setY(map.get("y"));
                    dong2Service.createDong(dong2);
                } else { // 좌표값이 있는 경우에는 그대로 사용하기
                    map.put("x", dong2.getX());
                    map.put("y", dong2.getY());
                }
            } else if (customer.getGu2() != null) { // gu가 있는 경우
                Gu2 gu2 = customer.getGu2();

                if (gu2.getGuName().isEmpty()) { // gu 이름이 없는 경우 삭제하기
                    gu2Service.deleteGu(gu2);
                } else if (gu2.getX() == null) { // 좌표값이 없는 경우 -> 구하기
                    map = findCoordinateByKakaoMap.findCoordinate(gu2.getGuName());
                    gu2.setX(map.get("x"));
                    gu2.setY(map.get("y"));
                    gu2Service.createGu(gu2);
                } else {
                    map.put("x", gu2.getX());
                    map.put("y", gu2.getY());
                }
            } else if (customer.getMetro2() != null) { // metro가 있는 경우
                Metro2 metro2 = customer.getMetro2();

                if (metro2.getMetroName().isEmpty()) { // metro 이름이 없는 경우 삭제하기
                    metro2Service.deleteMetro(metro2);
                } else if (metro2.getX() == null) { // 좌표값이 없는 경우 -> 구하기
                    map = findCoordinateByKakaoMap.findCoordinate(metro2.getMetroName());
                    metro2.setX(map.get("x"));
                    metro2.setY(map.get("y"));
                    metro2Service.createMetro(metro2);
                } else {
                    map.put("x", metro2.getX());
                    map.put("y", metro2.getY());
                }
            }

            list.add(map);
        }

        return list;
    }

    // 최신순 정렬 - 생성일 기준
    public List<Customer> findCustomerByLatest(long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers;

        if (customerTypePk == 0) { // 모든 고객유형 조회
            customers = customerRepository.findByEmployeeAndDelYnFalse(
                    employee,
                    Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
            );
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            return customerRepository.findByEmployeeAndCustomerTypeAndDelYnFalse(
                    employee,
                    customerType,
                    Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
            );
        }

        return customers;
    }

    // 월별 최신순 정렬 - 생성일 기준
    public List<Customer> findCustomerByLatest(LocalDate date, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers = new ArrayList<>();

        if (customerTypePk == 0) { // 모든 고객유형 조회
            List<CustomerType> customerTypes = customerTypeService.findCustomerTypeByCompanyFromEmployee(employee);
            List<CustomerType> dbCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.DB).collect(Collectors.toList());
            List<CustomerType> etcCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.ETC).collect(Collectors.toList());

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            customers = customerRepository.findAllByEmployeeAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(
                    employee,
                    Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                    parserStart(date).toLocalDate(),
                    parserFinish(date).toLocalDate(),
                    dbCustomerTypes
            );

            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            customers.addAll(customerRepository.findAllByEmployeeAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(
                    employee,
                    Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                    parserStart(date),
                    parserFinish(date),
                    etcCustomerTypes
            ));
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            if (customerTypeService.dataTypeisDB(customerType)) {
                customers = customerRepository.findAllByEmployeeAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(
                        employee,
                        Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                        parserStart(date).toLocalDate(),
                        parserFinish(date).toLocalDate(),
                        customerType
                );
            }

            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            if (customerTypeService.dataTypeisETC(customerType)) {
                customers = customerRepository.findAllByEmployeeAndCreatedAtBetweenAndCustomerTypeAndDelYnFalse(
                        employee,
                        Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                        parserStart(date),
                        parserFinish(date),
                        customerType
                );
            }
        }

        return customers;
    }

    // 나이값 반환
    public int returnAge(String age) {
        int start;
        if (age.equals("10")) start = 10;
        else if (age.equals("20")) start = 20;
        else if (age.equals("30")) start = 30;
        else if (age.equals("40")) start = 40;
        else if (age.equals("50")) start = 50;
        else if (age.equals("60")) start = 60;
        else if (age.equals("70")) start = 70;
        else if (age.equals("80")) start = 80;
        else throw new BusinessLogicException(ExceptionCode.CUSTOMER_AGE_FILTER_NOT_FOUND);

        return start;
    }

    // 나이별 정렬(1020, 3040, 5060, 7080)
    public List<Customer> findCustomerByAge(String age, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers;
        int start = Integer.parseInt(age)/100;
        int end = start + 19;
        System.out.println("!! start: " + start + ", end: " + end);

        if (customerTypePk == 0) { // 모든 고객유형 조회
            customers = customerRepository.findByEmployeeAndAgeBetweenAndDelYnFalseOrderByAge(
                    employee, start, end, Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
            );
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            customers = customerRepository.findByEmployeeAndAgeBetweenAndCustomerTypeAndDelYnFalseOrderByAge(
                    employee, start, end, customerType, Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
            );
        }

        return customers;
    }

    // 월별 나이별 정렬(1020, 3040, 5060, 7080)
    public List<Customer> findCustomerByAge(String age, LocalDate date, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers = new ArrayList<>();
        int ageStart = Integer.parseInt(age)/100;
        int ageEnd = ageStart + 19;

        if (customerTypePk == 0) { // 모든 고객유형 조회
            List<CustomerType> customerTypes = customerTypeService.findCustomerTypeByCompanyFromEmployee(employee);
            List<CustomerType> dbCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.DB).collect(Collectors.toList());
            List<CustomerType> etcCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.ETC).collect(Collectors.toList());

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            customers = customerRepository.findByEmployeeAndAgeBetweenAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalseOrderByAge(
                    employee,
                    ageStart,
                    ageEnd,
                    Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                    parserStart(date).toLocalDate(),
                    parserFinish(date).toLocalDate(),
                    dbCustomerTypes
            );

            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            customers.addAll(customerRepository.findByEmployeeAndAgeBetweenAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalseOrderByAge(
                    employee,
                    ageStart,
                    ageEnd,
                    Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                    parserStart(date),
                    parserFinish(date),
                    etcCustomerTypes
            ));
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            customers = new ArrayList<>();
            if (customerTypeService.dataTypeisDB(customerType)) {
                customers = customerRepository.findByEmployeeAndAgeBetweenAndRegisterDateBetweenAndCustomerTypeAndDelYnFalseOrderByAge(
                        employee,
                        ageStart,
                        ageEnd,
                        Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                        parserStart(date).toLocalDate(),
                        parserFinish(date).toLocalDate(),
                        customerType
                );
            }
            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            if (customerTypeService.dataTypeisETC(customerType)) {
                customers = customerRepository.findByEmployeeAndAgeBetweenAndCreatedAtBetweenAndCustomerTypeAndDelYnFalseOrderByAge(
                        employee,
                        ageStart,
                        ageEnd,
                        Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                        parserStart(date),
                        parserFinish(date),
                        customerType
                );
            }
        }

        return customers;
    }

    // 지역이름으로 정렬
    public List<Customer> findCustomerByDongName(String dongName, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers;

        if (customerTypePk == 0) { // 모든 고객유형 조회
            customers = customerRepository.findByEmployeeAndDongStringContainsAndDelYnFalse(
                    employee, dongName, Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("createdAt")) // 지역 오름차순, createdAt 내림차순
            );
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            customers = customerRepository.findByEmployeeAndDongStringContainsAndCustomerTypeAndDelYnFalse(
                    employee, dongName, customerType, Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("createdAt")) // 지역 오름차순, createdAt 내림차순
            );
        }

        return customers;
    }

    // 지역별 정렬
    public List<Customer> findCustomerByLi(Long dongPk, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers;
        String dongName = dong2Service.verifiedDong(dongPk).getDongName();

        if (customerTypePk == 0) { // 모든 고객유형 조회
            customers = customerRepository.findByEmployeeAndDongStringContainsAndDelYnFalse(
                    employee, dongName, Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("createdAt")) // 지역 오름차순, createdAt 내림차순
            );
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            customers = customerRepository.findByEmployeeAndDongStringContainsAndCustomerTypeAndDelYnFalse(
                    employee, dongName, customerType, Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("createdAt")) // 지역 오름차순, createdAt 내림차순
            );
        }


        return customers;
    }

    // 월별 지역별 정렬
    public List<Customer> findCustomerByLi(Long dongPk, LocalDate date, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        String dongName = dong2Service.verifiedDong(dongPk).getDongName();
        List<Customer> customers = new ArrayList<>();

        if (customerTypePk == 0) { // 모든 고객유형 조회
            List<CustomerType> customerTypes = customerTypeService.findCustomerTypeByCompanyFromEmployee(employee);
            List<CustomerType> dbCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.DB).collect(Collectors.toList());
            List<CustomerType> etcCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.ETC).collect(Collectors.toList());

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            customers = customerRepository.findByEmployeeAndDongStringContainsAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(
                    employee,
                    dongName,
                    Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("registerDate"), Sort.Order.desc("createdAt")), // 지역 오름차순, registerDate와 createdAt 내림차순
                    parserStart(date).toLocalDate(),
                    parserFinish(date).toLocalDate(),
                    dbCustomerTypes
            );

            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            customers.addAll(customerRepository.findByEmployeeAndDongStringContainsAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(
                    employee,
                    dongName,
                    Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("createdAt")), // 지역 오름차순, createdAt 내림차순
                    parserStart(date),
                    parserFinish(date),
                    etcCustomerTypes
            ));
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            customers = new ArrayList<>();
            if (customerTypeService.dataTypeisDB(customerType)) {
                customers = customerRepository.findByEmployeeAndDongStringContainsAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(
                        employee,
                        dongName,
                        Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("registerDate"), Sort.Order.desc("createdAt")), // 지역 오름차순, registerDate와 createdAt 내림차순
                        parserStart(date).toLocalDate(),
                        parserFinish(date).toLocalDate(),
                        customerType
                );
            }
            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            if (customerTypeService.dataTypeisETC(customerType)) {
                customers = customerRepository.findByEmployeeAndDongStringContainsAndCreatedAtBetweenAndCustomerTypeAndDelYnFalse(
                        employee,
                        dongName,
                        Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("createdAt")), // 지역 오름차순, createdAt 내림차순
                        parserStart(date),
                        parserFinish(date),
                        customerType
                );
            }
        }

        return customers;
    }

    // 계약여부 정렬(최신순)
    public List<Customer> findCustomerByContractYnByLatest(boolean contractYn, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers;

        if (customerTypePk == 0) { // 모든 고객유형 조회
            customers = customerRepository.findByEmployeeAndContractYnAndDelYnFalse(
                    employee,
                    contractYn,
                    Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
            );
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            customers = customerRepository.findByEmployeeAndContractYnAndCustomerTypeAndDelYnFalse(
                    employee,
                    contractYn,
                    customerType,
                    Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
            );
        }

        return customers;
    }

    // 계약여부 정렬(나이대별) - 1020, 3040, 5060, 7080
    public List<Customer> findCustomerByContractYnByLatest(boolean contractYn, String age, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers;
        int ageStart = Integer.parseInt(age)/100;
        int ageEnd = ageStart + 19;

        if (customerTypePk == 0) { // 모든 고객유형 조회
            customers = customerRepository.findByEmployeeAndContractYnAndAgeBetweenAndDelYnFalseOrderByAge(
                    employee,
                    contractYn,
                    Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                    ageStart,
                    ageEnd
            );
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            customers = customerRepository.findByEmployeeAndContractYnAndAgeBetweenAndCustomerTypeAndDelYnFalseOrderByAge(
                    employee,
                    contractYn,
                    Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                    ageStart,
                    ageEnd,
                    customerType
            );
        }

        return customers;
    }

    // 월별 계약여부 정렬
    public List<Customer> findCustomerByContractYn(boolean contractYn, LocalDate date, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers;

        if (customerTypePk == 0) { // 모든 고객유형 조회
            List<CustomerType> customerTypes = customerTypeService.findCustomerTypeByCompanyFromEmployee(employee);
            List<CustomerType> dbCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.DB).collect(Collectors.toList());
            List<CustomerType> etcCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.ETC).collect(Collectors.toList());

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            customers = customerRepository.findByEmployeeAndContractYnAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(
                    employee,
                    contractYn,
                    Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                    parserStart(date).toLocalDate(),
                    parserFinish(date).toLocalDate(),
                    dbCustomerTypes
            );

            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            customers.addAll(customerRepository.findByEmployeeAndContractYnAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(
                    employee,
                    contractYn,
                    Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                    parserStart(date),
                    parserFinish(date),
                    etcCustomerTypes
            ));
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            customers = new ArrayList<>();
            if (customerTypeService.dataTypeisDB(customerType)) {
                customers = customerRepository.findByEmployeeAndContractYnAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(
                        employee,
                        contractYn,
                        Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                        parserStart(date).toLocalDate(),
                        parserFinish(date).toLocalDate(),
                        customerType
                );
            }
            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            if (customerTypeService.dataTypeisETC(customerType)) {
                customers = customerRepository.findByEmployeeAndContractYnAndCreatedAtBetweenAndCustomerTypeAndDelYnFalse(
                        employee,
                        contractYn,
                        Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                        parserStart(date),
                        parserFinish(date),
                        customerType
                );
            }
        }

        return customers;
    }

    // 상담현황별 정렬
    public List<Customer> findCustomerByConsultationStatusByLatest(ConsultationStatus consultationStatus, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers;

        if (customerTypePk == 0) { // 모든 고객유형 조회
            customers = customerRepository.findByEmployeeAndConsultationStatusAndDelYnFalse(
                    employee,
                    consultationStatus,
                    Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
            );
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            customers = customerRepository.findByEmployeeAndConsultationStatusAndCustomerTypeAndDelYnFalse(
                    employee,
                    consultationStatus,
                    customerType,
                    Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
            );
        }

        return customers;
    }

    // 월별 상담현황별 정렬
    public List<Customer> findCustomerByConsultationStatusByLatestAndMonth(ConsultationStatus consultationStatus, LocalDate date, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers;

        if (customerTypePk == 0) { // 모든 고객유형 조회
            List<CustomerType> customerTypes = customerTypeService.findCustomerTypeByCompanyFromEmployee(employee);
            List<CustomerType> dbCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.DB).collect(Collectors.toList());
            List<CustomerType> etcCustomerTypes = customerTypes.stream().filter(customerType -> customerType.getDataType() == DataType.ETC).collect(Collectors.toList());

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            customers = customerRepository.findByEmployeeAndConsultationStatusAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(
                    employee,
                    consultationStatus,
                    Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                    parserStart(date).toLocalDate(),
                    parserFinish(date).toLocalDate(),
                    dbCustomerTypes
            );

            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            customers.addAll(customerRepository.findByEmployeeAndConsultationStatusAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(
                    employee,
                    consultationStatus,
                    Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                    parserStart(date),
                    parserFinish(date),
                    etcCustomerTypes
            ));
        } else { // 고객유형이 있는 경우
            CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

            // customerType.dataType = DB인 경우 -> registerDate 기준으로 월별 필터링 & 정렬
            customers = new ArrayList<>();
            if (customerTypeService.dataTypeisDB(customerType)) {
                customers = customerRepository.findByEmployeeAndConsultationStatusAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(
                        employee,
                        consultationStatus,
                        Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                        parserStart(date).toLocalDate(),
                        parserFinish(date).toLocalDate(),
                        customerType
                );
            }
            // customerType.dataType = ETC인 경우 -> createdAt 기준으로 월별 필터링 & 정렬
            if (customerTypeService.dataTypeisETC(customerType)) {
                customers = customerRepository.findByEmployeeAndConsultationStatusAndCreatedAtBetweenAndCustomerTypeAndDelYnFalse(
                        employee,
                        consultationStatus,
                        Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                        parserStart(date),
                        parserFinish(date),
                        customerType
                );
            }
        }

        return customers;
    }

    // 이름 검색
    public List<Customer> findCustomerByName(String name) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        return customerRepository.findByEmployeeAndNameContainsAndDelYnFalse(employee, name);
    }

    // UPDATE
    public Customer patchCustomer(Customer customer, CustomerRequestDto.MetroGuDong metroGuDong, Long customerTypePk) {
        Customer findCustomer = verifiedCustomer(customer.getPk());
        Employee employee = findEmployee.getLoginEmployeeByToken();
        // 직원 유효성 검사
        if (findCustomer.getEmployee().getPk() != employee.getPk())
            throw new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_CONTAIN_CUSTOMER);

        // liPk를 이용한 dongString 자동 설정
//        if (liPk != 0) {
//            Li li = liService.findLi(liPk);
//            findCustomer.setLi(li);
//            findCustomer.setDongString(liService.findDongString(li));
//        }

        // metro, gu, dong을 이용한 dongString 자동 설정
        makeDongString(metroGuDong, findCustomer);

        // 고객 유형 설정
        if (customerTypePk != null) findCustomer.setCustomerType(customerTypeService.findCustomerType(customerTypePk));

        Optional.ofNullable(customer.getName()).ifPresent(findCustomer::setName);
        if (customer.getAge() != 0) findCustomer.setAge(customer.getAge());
        Optional.ofNullable(customer.getBirth()).ifPresent(birth -> {
            // 만나이 계산
            findCustomer.setAge(CalculateAge.getAge(birth));
            findCustomer.setBirth(birth);
        });
        Optional.ofNullable(customer.getAddress()).ifPresent(findCustomer::setAddress);
        Optional.ofNullable(customer.getPhone()).ifPresent(findCustomer::setPhone);
        Optional.ofNullable(customer.getMemo()).ifPresent(findCustomer::setMemo);
        Optional.ofNullable(customer.getState()).ifPresent(findCustomer::setState);
        Optional.ofNullable(customer.getContractYn()).ifPresent(findCustomer::setContractYn);
        Optional.ofNullable(customer.getRegisterDate()).ifPresent(findCustomer::setRegisterDate);
        Optional.ofNullable(customer.getDelYn()).ifPresent(findCustomer::setDelYn);
        Optional.ofNullable(customer.getEmail()).ifPresent(findCustomer::setEmail);
        Optional.ofNullable(customer.getWork()).ifPresent(findCustomer::setWork);
        Optional.ofNullable(customer.getWorry()).ifPresent(findCustomer::setWorry);
        if(customer.getSalary() != 0) findCustomer.setSalary(customer.getSalary());
        Optional.ofNullable(customer.getWorkTime()).ifPresent(findCustomer::setWorkTime);
        Optional.ofNullable(customer.getGender()).ifPresent(findCustomer::setGender);

        // 상담현황 변경할 때, 상담현황 수정일자도 변경
        Optional.ofNullable(customer.getConsultationStatus()).ifPresent(consultationStatus -> {
            // 날짜 변경
            LocalDateTime now = LocalDateTime.now();
            if(customer.getConsultationStatus() != findCustomer.getConsultationStatus()){
                findCustomer.setConsultationStatusModifiedAt(now);
            }

            // 상담현황 변경
            findCustomer.setConsultationStatus(consultationStatus);
        });
        if (Optional.ofNullable(customer.getDelYn()).orElse(false)) {
            changeSchduleDelYnTrue(findCustomer);
        }

        return customerRepository.save(findCustomer);
    }

    // DELETE
//    public void deleteCustomer(long customerPk) {
//        Customer customer = verifiedCustomer(customerPk);
//        customerRepository.delete(customer);
//    }

    // delYn=true 변경 후 customer과 관련된 schedule.delYn=true로 변경
    public void deleteCustomer(long customerPk) {
        Customer customer = verifiedCustomer(customerPk);
        customer.setDelYn(true);
        changeSchduleDelYnTrue(customer);
        customerRepository.save(customer);
    }

    // 검증
    public Customer verifiedCustomer(long customerPk) {
        Optional<Customer> customer = customerRepository.findByPkAndDelYnFalse(customerPk);
        return customer.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CUSTOMER_NOT_FOUND));
    }

    // parser - 달의 첫번째날
    public LocalDateTime parserStart(LocalDate start) {
        return start.atStartOfDay().withDayOfMonth(1);
    }

    // parser - 달의 마지막날
    public LocalDateTime parserFinish(LocalDate finish) {
        return finish.atTime(LocalTime.MAX).withDayOfMonth(finish.lengthOfMonth());
    }

    // customer의 delYn=false 할 때, 관련 schedule(history)의 delYn=false로 변경
    public void changeSchduleDelYnTrue(Customer customer) {
        for (Schedule schedule : customer.getSchedules()) {
            schedule.setDelYn(true);
        }
    }

    // metro, gu, dong을 이용한 dongString 자동 설정
    public Customer makeDongString(String dongString, Customer customer) {
        CustomerRequestDto.MetroGuDong metroGuDong = new CustomerRequestDto.MetroGuDong();
        String[] dongStrings = dongString.split(" ");
        if (dongStrings.length >= 1) metroGuDong.setMetroName(dongStrings[0]);
        if (dongStrings.length >= 2) metroGuDong.setGuName(dongStrings[1]);
        if (dongStrings.length >= 3) metroGuDong.setDongName(dongStrings[2]);

        return makeDongString(metroGuDong, customer);
    }


    // metro, gu, dong을 이용한 dongString 자동 설정
    public Customer makeDongString(CustomerRequestDto.MetroGuDong metroGuDong, Customer customer) {
        String dongString = "";

        // metro, gu, dong을 이용한 dongString 생성
        if (metroGuDong != null) {
            String metroName = metroGuDong.getMetroName();
            Metro2 metro2 = null;
            if (metroName != null && !metroName.isEmpty()) {
                dongString += metroName + " ";
                // metro, gu, dong 자동 설정
                metro2 = metro2Service.findMetroByMetroName(metroName);
                if (metro2 == null) metro2 = metro2Service.createMetro(metroName);
                customer.setMetro2(metro2);
            }

            String guName = metroGuDong.getGuName();
            Gu2 gu2 = null;
            if (guName != null && !guName.isEmpty()) {
                dongString += guName + " ";
                // metro, gu, dong 자동 설정
                gu2 = gu2Service.findGuByGuNameAndMetro(guName, metro2);
                if (gu2 == null) gu2 = gu2Service.createGu(guName, metro2);
                customer.setGu2(gu2);
            }

            String dongName = metroGuDong.getDongName();
            if (dongName != null && !dongName.isEmpty()) {
                dongString += dongName + " ";
                // metro, gu, dong 자동 설정
                Dong2 dong2 = dong2Service.findDongByDongNameAndGu(dongName, gu2);
                if (dong2 == null) dong2 = dong2Service.createDong(dongName, gu2);
                customer.setDong2(dong2);
            }
        }

        // 상세주소를 dongStirng에 추가
//        if(customer.getAddress() != null) dongString += customer.getAddress();
        if (!dongString.equals("")) customer.setDongString(dongString);

        return customer;
    }
}