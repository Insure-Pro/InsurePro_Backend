package ga.backend.customerType.service;

import ga.backend.company.entity.Company;
import ga.backend.company.service.CompanyService;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.repository.CustomerTypeRepository;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerTypeService {
    private final CustomerTypeRepository customerTypeRepository;
    private final CompanyService companyService;
    private final FindEmployee findEmployee;

    // CREATE
    @Transactional
    public CustomerType createCustomerType(CustomerType customerType) {
        customerType.setName(customerType.getName().toUpperCase()); // 고객유형 이름은 무조건 대문자만 허용

        Employee employee = findEmployee.getLoginEmployeeByToken();
        customerType.setEmployeePk(employee.getPk());
        customerType.setCompany(employee.getCompany());
        return customerTypeRepository.save(customerType);
    }

    // READ
    public CustomerType findCustomerType(long customerTypePk) {
        return verifiedCustomerType(customerTypePk);
    }

    // UPDATE
    public CustomerType patchCustomerType(CustomerType customerType) {
        CustomerType findCustomerType = verifiedCustomerType(customerType.getPk());

        Employee employee = findEmployee.getLoginEmployeeByToken();
        findCustomerType.setEmployeePk(employee.getPk());
        findCustomerType.setCompany(employee.getCompany());

        Optional.ofNullable(customerType.getName()).ifPresent(findCustomerType::setName);
        Optional.ofNullable(customerType.getColor()).ifPresent(findCustomerType::setColor);
        Optional.ofNullable(customerType.getDetail()).ifPresent(findCustomerType::setDetail);
        Optional.ofNullable(customerType.getDataType()).ifPresent(findCustomerType::setDataType);
        Optional.ofNullable(customerType.getDelYn()).ifPresent(findCustomerType::setDelYn);
        return customerTypeRepository.save(findCustomerType);
    }

    // DELETE
    public void deleteCustomerType(long customerTypePk) {
        CustomerType customerType = verifiedCustomerType(customerTypePk);
        Employee employee = findEmployee.getLoginEmployeeByToken();
        if(customerType.getCustomers().isEmpty()) // customerType을 사용하는 customer가 없는 경우
            customerTypeRepository.delete(customerType); // 완전 삭제
        else { // customerType을 사용하는 customer가 있는 경우
            customerType.setEmployeePk(employee.getPk());
            customerType.setDelYn(true); // 삭제여부 = true로 변경
            customerTypeRepository.save(customerType);
        }
    }

    // 검증
    public CustomerType verifiedCustomerType(long customerTypePk) {
        Optional<CustomerType> customerTypes = customerTypeRepository.findById(customerTypePk);
        return customerTypes.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CUSTOMERTYPE_NOT_FOUND));
    }
}
