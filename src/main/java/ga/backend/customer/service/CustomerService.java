package ga.backend.customer.service;

import ga.backend.customer.entity.Customer;
import ga.backend.customer.repository.CustomerRepository;
import ga.backend.dong.service.DongService;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.li.entity.Li;
import ga.backend.li.service.LiService;
import ga.backend.schedule.entity.Schedule;
import ga.backend.util.FindEmployee;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final LiService liService;
    private final DongService dongService;

    private final FindEmployee findEmployee;

    private final List<Customer.CustomerType> customerTypesRegisterDate = List.of(
            Customer.CustomerType.OD,
            Customer.CustomerType.AD,
            Customer.CustomerType.CP,
            Customer.CustomerType.CD,
            Customer.CustomerType.JD
    );
    private final List<Customer.CustomerType> customerTypesCreatedAt = List.of(
            Customer.CustomerType.H,
            Customer.CustomerType.X,
            Customer.CustomerType.Y,
            Customer.CustomerType.Z
    );

    public CustomerService(CustomerRepository customerRepository, LiService liService, DongService dongService, FindEmployee findEmployee) {
        this.customerRepository = customerRepository;
        this.liService = liService;
        this.dongService = dongService;
        this.findEmployee = findEmployee;
    }

    // CREATE
    public Customer createCustomer(Customer customer, long liPk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        customer.setEmployee(employee);

        if (liPk != 0) {
            Li li = liService.findLi(liPk);
            customer.setLi(li);
            customer.setDongString(liService.findDongString(li));
        }

        return customerRepository.save(customer);
    }

    // READ
    public Customer findCustomer(long customerPk) {
        Customer customer = verifiedCustomer(customerPk);
        return customer;
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
        List<Customer> customers = customerRepository.findAllByEmployeeAndRegisterDateBetweenAndCustomerTypeIn(
                    employee,
                    Sort.by(Sort.Direction.DESC, "registerDate", "createdAt"), // 내림차순
                    parserStart(date).toLocalDate(),
                    parserFinish(date).toLocalDate(),
                    customerTypesRegisterDate
            );
        // createdAt 기준으로 월별 필터링 & 정렬 → customers에 추가하기
        customers.addAll(customerRepository.findAllByEmployeeAndCreatedAtBetweenAndCustomerTypeIn(
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
        return customerRepository.findByEmployeeAndName(employee, name);
    }

    // UPDATE
    public Customer patchCustomer(Customer customer, long liPk) {
        Customer findCustomer = verifiedCustomer(customer.getPk());
        Employee employee = findEmployee.getLoginEmployeeByToken();
        // 직원 유효성 검사
        if (findCustomer.getEmployee().getPk() != employee.getPk())
            throw new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_CONTAIN_CUSTOMER);
        if (liPk != 0) {
            Li li = liService.findLi(liPk);
            findCustomer.setLi(li);
            findCustomer.setDongString(liService.findDongString(li));
        }

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
}