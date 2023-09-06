package ga.backend.customer.service;

import ga.backend.customer.entity.Customer;
import ga.backend.customer.repository.CustomerRepository;
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
    private final LiService liService;
    private final DongService dongService;
    private final FindEmployee findEmployee;

    // CREATE
    public Customer createCustomer(Customer customer, long customerTypePk, long liPk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        customer.setEmployee(employee);
        customer.setCustomerType(customerTypeService.findCustomerType(customerTypePk));

        Li li = liService.findLi(liPk);
        customer.setLi(li);
        customer.setDongString(liService.findDongString(li));

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

        if (age.equals("2030")) start = 20;
        else if (age.equals("4050")) start = 40;
        else if (age.equals("6070")) start = 60;

        int end = start + 19;

        List<Customer> customers = customerRespository.findByEmployeeAndAgeBetween(
                employee, start, end, Sort.by(Sort.Direction.ASC, "age") // 오름차순
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
    public Customer patchCustomer(Customer customer) {
        Customer findCustomer = verifiedCustomer(customer.getPk());
        Optional.ofNullable(customer.getName()).ifPresent(findCustomer::setName);
        Optional.ofNullable(customer.getBirth()).ifPresent(findCustomer::setBirth);
        Optional.ofNullable(customer.getAddress()).ifPresent(findCustomer::setAddress);
        Optional.ofNullable(customer.getPhone()).ifPresent(findCustomer::setPhone);
        Optional.ofNullable(customer.getMemo()).ifPresent(findCustomer::setMemo);
        Optional.ofNullable(customer.getContractYn()).ifPresent(findCustomer::setContractYn);
        Optional.ofNullable(customer.getIntensiveCareStartDate()).ifPresent(findCustomer::setIntensiveCareStartDate);
        Optional.ofNullable(customer.getIntensiveCareFinishDate()).ifPresent(findCustomer::setIntensiveCareFinishDate);
        Optional.ofNullable(customer.getRegisterDate()).ifPresent(findCustomer::setRegisterDate);
        Optional.ofNullable(customer.getDelYn()).ifPresent(findCustomer::setDelYn);
        if (customer.getAge() != 0) findCustomer.setAge(customer.getAge());


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
