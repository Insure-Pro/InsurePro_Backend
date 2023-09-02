package ga.backend.customer.service;

import ga.backend.customer.entity.Customer;
import ga.backend.customer.repository.CustomerRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRespository;

    // CREATE
    public Customer createCustomer(Customer customer) {
        return customerRespository.save(customer);
    }

    // READ
    public Customer findCustomer(long customerPk) {
        Customer customer = verifiedCustomer(customerPk);
        return customer;
    }

    // UPDATE
    public Customer patchCustomer(Customer customer) {
        Customer findCustomer = verifiedCustomer(customer.getPk());
        Optional.ofNullable(customer.getName()).ifPresent(findCustomer::setName);
        Optional.ofNullable(customer.getBirth()).ifPresent(findCustomer::setBirth);
        Optional.ofNullable(customer.getDongString()).ifPresent(findCustomer::setDongString);
        Optional.ofNullable(customer.getAddress()).ifPresent(findCustomer::setAddress);
        Optional.ofNullable(customer.getPhone()).ifPresent(findCustomer::setPhone);
        Optional.ofNullable(customer.getMemo()).ifPresent(findCustomer::setMemo);
        Optional.ofNullable(customer.getContractYn()).ifPresent(findCustomer::setContractYn);
        Optional.ofNullable(customer.getIntensiveCareStartDate()).ifPresent(findCustomer::setIntensiveCareStartDate);
        Optional.ofNullable(customer.getIntensiveCareFinishDate()).ifPresent(findCustomer::setIntensiveCareFinishDate);
        Optional.ofNullable(customer.getRegisterDate()).ifPresent(findCustomer::setRegisterDate);
        Optional.ofNullable(customer.getDelYn()).ifPresent(findCustomer::setDelYn);
        if(customer.getAge() != 0) findCustomer.setAge(customer.getAge());


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
