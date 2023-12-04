package ga.backend.customer.service;

import ga.backend.customer.dto.CustomerRequestDto;
import ga.backend.customer.entity.Customer;
import ga.backend.customer.repository.CustomerRepository;
import ga.backend.dong.service.DongService;
import ga.backend.dong2.entity.Dong2;
import ga.backend.dong2.service.Dong2Service;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.gu2.entity.Gu2;
import ga.backend.gu2.service.Gu2Service;
import ga.backend.li.entity.Li;
import ga.backend.li.service.LiService;
import ga.backend.metro2.entity.Metro2;
import ga.backend.metro2.service.Metro2Service;
import ga.backend.schedule.entity.Schedule;
import ga.backend.util.CustomerType;
import ga.backend.util.FindCoordinateByKakaoMap;
import ga.backend.util.FindEmployee;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final LiService liService;
    private final DongService dongService;
    private final Dong2Service dong2Service;
    private final Gu2Service gu2Service;
    private final Metro2Service metro2Service;

    private final FindEmployee findEmployee;
    private final FindCoordinateByKakaoMap findCoordinateByKakaoMap;

    public CustomerService(CustomerRepository customerRepository,
                           LiService liService,
                           DongService dongService,
                           Dong2Service dong2Service,
                           Gu2Service gu2Service,
                           Metro2Service metro2Service,
                           FindEmployee findEmployee,
                           FindCoordinateByKakaoMap findCoordinateByKakaoMap) {
        this.customerRepository = customerRepository;
        this.liService = liService;
        this.dongService = dongService;
        this.dong2Service = dong2Service;
        this.gu2Service = gu2Service;
        this.metro2Service = metro2Service;
        this.findEmployee = findEmployee;
        this.findCoordinateByKakaoMap = findCoordinateByKakaoMap;
    }

    private final List<CustomerType> customerTypesRegisterDate = List.of(
            CustomerType.OD,
            CustomerType.AD,
            CustomerType.CP,
            CustomerType.CD,
            CustomerType.JD
    );
    private final List<CustomerType> customerTypesCreatedAt = List.of(
            CustomerType.H,
            CustomerType.X,
            CustomerType.Y,
            CustomerType.Z
    );

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

        if(customer.getContractYn() == null) customer.setContractYn(false);
        System.out.println("!! contractYn : " + customer.getContractYn());

        // metro, gu, dong을 이용한 dongString 자동 설정
        makeDongString(metroGuDong, customer);

        return customerRepository.save(customer);
    }

    public List<Customer> createCustomers(List<Customer> customers) {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        for(int i=0; i<customers.size(); i++) {
            customers.get(i).setEmployee(employee);

            if(customers.get(i).getContractYn() == null) customers.get(i).setContractYn(false);

            // metro, gu, dong을 이용한 dongString 자동 설정
            if(customers.get(i).getDongString() != null) {
                makeDongString(customers.get(i).getDongString(), customers.get(i));
            }

            customerRepository.save(customers.get(i));
        }

        return customers;
    }


    // READ
    public Customer findCustomer(long customerPk) {
        Customer customer = verifiedCustomer(customerPk);
        return customer;
    }

    public List<Map<String, Double>> findCoordinate(List<Customer> customers) {
        List<Map<String, Double>> list = new ArrayList<>();

        for(Customer customer : customers) {
            Map<String, Double> map = new HashMap<>();

            if(customer.getDong2() != null) { // dong이 있는 경우
                Dong2 dong2 = customer.getDong2();

                // 좌표값이 없는 경우 -> 구하기
                if(dong2.getX() == null) {
                    map = findCoordinateByKakaoMap.findCoordinate(dong2.getDongName());
                    dong2.setX(map.get("x"));
                    dong2.setY(map.get("y"));
                    dong2Service.createDong(dong2);
                } else {
                    map.put("x", dong2.getX());
                    map.put("y", dong2.getY());
                }
            } else if(customer.getGu2() != null) { // gu가 있는 경우
                Gu2 gu2 = customer.getGu2();

                // 좌표값이 없는 경우 -> 구하기
                if(gu2.getX() == null) {
                    map = findCoordinateByKakaoMap.findCoordinate(gu2.getGuName());
                    gu2.setX(map.get("x"));
                    gu2.setY(map.get("y"));
                    gu2Service.createGu(gu2);
                } else {
                    map.put("x", gu2.getX());
                    map.put("y", gu2.getY());
                }
            } else if(customer.getMetro2() != null) { // metro가 있는 경우
                Metro2 metro2 = customer.getMetro2();

                // 좌표값이 없는 경우 -> 구하기
                if(metro2.getX() == null) {
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
    public List<Customer> findCustomerByLatest() {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        return customerRepository.findByEmployeeAndDelYnFalse(
                employee, Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
        );
    }

    // 월별 최신순 정렬 - 생성일 기준
    public List<Customer> findCustomerByLatest(LocalDate date) {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        // registerDate 기준으로 월별 필터링 & 정렬
        List<Customer> customers = customerRepository.findAllByEmployeeAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(
                employee,
                Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                parserStart(date).toLocalDate(),
                parserFinish(date).toLocalDate(),
                customerTypesRegisterDate
        );
        // createdAt 기준으로 월별 필터링 & 정렬 → customers에 추가하기
        customers.addAll(customerRepository.findAllByEmployeeAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(
                employee,
                Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                parserStart(date),
                parserFinish(date),
                customerTypesCreatedAt
        ));

        return customers;
    }

    // 나이별 정렬(1020, 3040, 5060, 7080)
    public List<Customer> findCustomerByAge(String age) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        int start = 0;

        if (age.equals("1020")) start = 10;
        else if (age.equals("3040")) start = 30;
        else if (age.equals("5060")) start = 50;
        else if (age.equals("7080")) start = 70;
        else throw new BusinessLogicException(ExceptionCode.CUSTOMER_AGE_FILTER_NOT_FOUND);

        int end = start + 19;

        return customerRepository.findByEmployeeAndAgeBetweenAndDelYnFalseOrderByAge(
                employee, start, end, Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
        );
    }

    // 월별 나이별 정렬(1020, 3040, 5060, 7080)
    public List<Customer> findCustomerByAge(String age, LocalDate date) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        int ageStart = 0;

        if (age.equals("1020")) ageStart = 10;
        else if (age.equals("3040")) ageStart = 30;
        else if (age.equals("5060")) ageStart = 50;
        else if (age.equals("7080")) ageStart = 70;
        else throw new BusinessLogicException(ExceptionCode.CUSTOMER_AGE_FILTER_NOT_FOUND);

        int ageEnd = ageStart + 19;

        // registerDate 기준으로 월별 필터링 & 정렬
        List<Customer> customers = customerRepository.findByEmployeeAndAgeBetweenAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalseOrderByAge(
                employee,
                ageStart,
                ageEnd,
                Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                parserStart(date).toLocalDate(),
                parserFinish(date).toLocalDate(),
                customerTypesRegisterDate
        );
        // createdAt 기준으로 월별 필터링 & 정렬 → customers에 추가하기
        customers.addAll(customerRepository.findByEmployeeAndAgeBetweenAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalseOrderByAge(
                employee,
                ageStart,
                ageEnd,
                Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                parserStart(date),
                parserFinish(date),
                customerTypesCreatedAt
        ));

        return customers;
    }

    // 지역별 정렬
    public List<Customer> findCustomerByLi(Long dongPk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        String dongName = dongService.verifiedDong(dongPk).getDongName();

        return customerRepository.findByEmployeeAndDongStringContainsAndDelYnFalse(
                employee, dongName, Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("createdAt")) // 지역 오름차순, createdAt 내림차순
        );
    }

    // 월별 지역별 정렬
    public List<Customer> findCustomerByLi(Long dongPk, LocalDate date) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        String dongName = dongService.verifiedDong(dongPk).getDongName();

        // registerDate 기준으로 월별 필터링 & 정렬
        List<Customer> customers = customerRepository.findByEmployeeAndDongStringContainsAndRegisterDateBetweenAndDelYnFalse(
                employee,
                dongName,
                Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("registerDate"), Sort.Order.desc("createdAt")), // 지역 오름차순, registerDate와 createdAt 내림차순
                parserStart(date).toLocalDate(),
                parserFinish(date).toLocalDate(),
                customerTypesRegisterDate
        );
        // createdAt 기준으로 월별 필터링 & 정렬 → customers에 추가하기
        customers.addAll(customerRepository.findByEmployeeAndDongStringContainsAndCreatedAtBetweenAndDelYnFalse(
                employee,
                dongName,
                Sort.by(Sort.Order.asc("li_pk"), Sort.Order.desc("createdAt")), // 지역 오름차순, createdAt 내림차순
                parserStart(date),
                parserFinish(date),
                customerTypesCreatedAt
        ));

        return customers;
    }

    // 계약여부 정렬(최신순)
    public List<Customer> findCustomerByContractYnByLatest(boolean contractYn) {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        return customerRepository.findByEmployeeAndContractYnAndDelYnFalse(
                employee,
                contractYn,
                Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
        );
    }

    // 계약여부 정렬(나이대별)
    public List<Customer> findCustomerByContractYnByLatest(boolean contractYn, String age) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        int ageStart = 0;

        if (age.equals("1020")) ageStart = 10;
        else if (age.equals("3040")) ageStart = 30;
        else if (age.equals("5060")) ageStart = 50;
        else if (age.equals("7080")) ageStart = 70;
        else throw new BusinessLogicException(ExceptionCode.CUSTOMER_AGE_FILTER_NOT_FOUND);

        int ageEnd = ageStart + 19;

        return customerRepository.findByEmployeeAndContractYnAndAgeBetweenAndDelYnFalseOrderByAge(
                employee,
                contractYn,
                Sort.by(Sort.Direction.DESC, "createdAt"), // 내림차순
                ageStart,
                ageEnd
        );
    }

    // 월별 계약여부 정렬
    public List<Customer> findCustomerByContractYn(boolean contractYn, LocalDate date) {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        return customerRepository.findByEmployeeAndContractYnAndCreatedAtBetweenAndDelYnFalse(
                employee,
                contractYn,
                parserStart(date),
                parserFinish(date)
        );
    }

    // 이름 검색
    public List<Customer> findCustomerByName(String name) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        return customerRepository.findByEmployeeAndNameAndDelYnFalse(employee, name);
    }

    // UPDATE
    public Customer patchCustomer(Customer customer, CustomerRequestDto.MetroGuDong metroGuDong) {
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

        Optional.ofNullable(customer.getCustomerType()).ifPresent(findCustomer::setCustomerType);
        Optional.ofNullable(customer.getName()).ifPresent(findCustomer::setName);
        Optional.ofNullable(customer.getBirth()).ifPresent(findCustomer::setBirth);
        if (customer.getAge() != 0) findCustomer.setAge(customer.getAge());
        Optional.ofNullable(customer.getAddress()).ifPresent(findCustomer::setAddress);
        Optional.ofNullable(customer.getPhone()).ifPresent(findCustomer::setPhone);
        Optional.ofNullable(customer.getMemo()).ifPresent(findCustomer::setMemo);
        Optional.ofNullable(customer.getState()).ifPresent(findCustomer::setState);
        Optional.ofNullable(customer.getContractYn()).ifPresent(findCustomer::setContractYn);
        Optional.ofNullable(customer.getRegisterDate()).ifPresent(findCustomer::setRegisterDate);
        Optional.ofNullable(customer.getDelYn()).ifPresent(findCustomer::setDelYn);
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
        if(dongStrings.length >= 1) metroGuDong.setMetroName(dongStrings[0]);
        if(dongStrings.length >= 2) metroGuDong.setGuName(dongStrings[1]);
        if(dongStrings.length >= 3) metroGuDong.setDongName(dongStrings[2]);

        return makeDongString(metroGuDong, customer);
    }


    // metro, gu, dong을 이용한 dongString 자동 설정
    public Customer makeDongString(CustomerRequestDto.MetroGuDong metroGuDong, Customer customer) {
        String dongString = "";

        // metro, gu, dong을 이용한 dongString 생성
        if(metroGuDong != null) {
            String metroName = metroGuDong.getMetroName();
            Metro2 metro2 = null;
            if(metroName != null) {
                dongString += metroName + " ";
                // metro, gu, dong 자동 설정
                metro2 = metro2Service.findMetroByMetroName(metroName);
                if(metro2 == null) metro2 = metro2Service.createMetro(metroName);
                customer.setMetro2(metro2);
            }

            String guName = metroGuDong.getGuName();
            Gu2 gu2 = null;
            if(guName != null) {
                dongString += guName + " ";
                // metro, gu, dong 자동 설정
                gu2 = gu2Service.findGuByGuNameAndMetro(guName, metro2);
                if(gu2 == null) gu2 = gu2Service.createGu(guName, metro2);
                customer.setGu2(gu2);
            }

            String dongName = metroGuDong.getDongName();
            if(dongName != null) {
                dongString += dongName + " ";
                // metro, gu, dong 자동 설정
                Dong2 dong2 = dong2Service.findDongByDongNameAndGu(dongName, gu2);
                if(dong2 == null) dong2 = dong2Service.createDong(dongName, gu2);
                customer.setDong2(dong2);
            }
        }

        // 상세주소를 dongStirng에 추가
//        if(customer.getAddress() != null) dongString += customer.getAddress();
        if(!dongString.equals("")) customer.setDongString(dongString);

        return customer;
    }
}