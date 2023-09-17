package ga.backend.customer.service;

import ga.backend.customer.entity.Customer;
import ga.backend.customer.repository.CustomerRepository;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.repository.CustomerTypeRepository;
import ga.backend.customerType.service.CustomerTypeService;
import ga.backend.dong.service.DongService;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.li.entity.Li;
import ga.backend.li.service.LiService;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerTypeService customerTypeService;
    private final CustomerRepository customerRespository;
    private final CustomerTypeRepository customerTypeRepository;
    private final LiService liService;
    private final DongService dongService;
    private final FindEmployee findEmployee;

    // CREATE
    public Customer createCustomer(Customer customer, String customerTypeName, long liPk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        customer.setEmployee(employee);
        CustomerType customerType = customerTypeRepository
                .findTopByDelYnAndType(false, customerTypeName)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CUSTOM_TYPE_NOT_FOUND));
        System.out.println("여기");
        System.out.println(customerType.getDetail());
        customer.setCustomerType(customerType);

        if(liPk != 0) {
            Li li = liService.findLi(liPk);
            customer.setLi(li);
            customer.setDongString(liService.findDongString(li));
        }

        return customerRespository.save(customer);
    }

    // READ
    public Customer findCustomer(long customerPk) {
        Customer customer = verifiedCustomer(customerPk);
        return customer;
    }

    // 최신순 정렬 - 생성일 기준
    public List<Customer> findCustomerByLatest() {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        List<Customer> customers = customerRespository.findAllByEmployee(
                employee, Sort.by(Sort.Direction.DESC, "createdAt") // 내림차순
        );
        return customers;
    }

    // 나이별 정렬(2030, 4050, 6070)
    public List<Customer> findCustomerByAge(String age) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        int start = 0;

        if (age.equals("1020")) start = 10;
        else if (age.equals("3040")) start = 30;
        else if (age.equals("5060")) start = 50;

        int end = start + 19;

        List<Customer> customers = customerRespository.findByEmployeeAndAgeBetween(
                employee, start, end, Sort.by(Sort.Direction.DESC, "createdAt") // 오름차순
        );
        return customers;
    }

    // 지역별 정렬
    public List<Customer> findCustomerByLi(Long dongPk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        String dongName = dongService.verifiedDong(dongPk).getDongName();

        List<Customer> customers = customerRespository.findByEmployeeAndDongStringContains(
                employee, dongName, Sort.by(Sort.Direction.ASC, "li_pk") // 오름차순
        );
        return customers;
    }

    // 계약여부 정렬
    public List<Customer> findCustomerByContractYn(boolean contractYn) {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        List<Customer> customers = customerRespository.findByEmployeeAndContractYn(
                employee, contractYn
        );
        return customers;
    }

    // 관리 고객 정렬
    public List<Customer> findCustomerByIntensiveCare() {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        List<Customer> customers =
                customerRespository.findByEmployeeAndIntensiveCareExists(
                        employee
                );
        return customers;
    }

    // UPDATE
    public Customer patchCustomer(Customer customer, String customerTypeName, long liPk) {
        Customer findCustomer = verifiedCustomer(customer.getPk());
        Employee employee = findEmployee.getLoginEmployeeByToken();
        // 직원 유효성 검사
        if(findCustomer.getEmployee().getPk() != employee.getPk())
            throw new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_CONTAIN_CUSTOMER);
        if(customerTypeName != null) {
            findCustomer.setCustomerType(
                    customerTypeRepository.findTopByDelYnAndType(false, customerTypeName)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CUSTOM_TYPE_NOT_FOUND))
            );
        }
        if(liPk != 0) {
            Li li = liService.findLi(liPk);
            findCustomer.setLi(li);
            findCustomer.setDongString(liService.findDongString(li));
        }

        Optional.ofNullable(customer.getName()).ifPresent(findCustomer::setName);
        Optional.ofNullable(customer.getBirth()).ifPresent(findCustomer::setBirth);
        if (customer.getAge() != 0) findCustomer.setAge(customer.getAge());
        Optional.ofNullable(customer.getAddress()).ifPresent(findCustomer::setAddress);
        Optional.ofNullable(customer.getPhone()).ifPresent(findCustomer::setPhone);
        Optional.ofNullable(customer.getMemo()).ifPresent(findCustomer::setMemo);
        Optional.ofNullable(customer.getContractYn()).ifPresent(findCustomer::setContractYn);
        Optional.ofNullable(customer.getDelYn()).ifPresent(findCustomer::setDelYn);
        Optional.ofNullable(customer.getIntensiveCareStartDate()).ifPresent(findCustomer::setIntensiveCareStartDate);
        Optional.ofNullable(customer.getIntensiveCareFinishDate()).ifPresent(findCustomer::setIntensiveCareFinishDate);
        Optional.ofNullable(customer.getRegisterDate()).ifPresent(findCustomer::setRegisterDate);

        return customerRespository.save(findCustomer);
    }

    // DELETE
    public void deleteCustomer(long customerPk) {
        Customer customer = verifiedCustomer(customerPk);
        customerRespository.delete(customer);
    }

    // 검증
    public Customer verifiedCustomer(long customerPk) {
        Optional<Customer> customer = customerRespository.findById(customerPk);
        return customer.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMPANY_NOT_FOUND));
    }
}
