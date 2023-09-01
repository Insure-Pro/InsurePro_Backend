package ga.backend.customerType.service;

import ga.backend.company.entity.Company;
import ga.backend.company.repository.CompanyRepository;
import ga.backend.company.service.CompanyService;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.repository.CustomerTypeRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerTypeService {
    private final CustomerTypeRepository customerTypeRespository;
    private final CompanyService companyService;


    // CREATE
    public CustomerType createCustomerType(CustomerType customerType, Long company_pk) {
        customerType.setCompany(companyService.verifiedCompany(company_pk));
        return customerTypeRespository.save(customerType);
    }

    // READ
    public CustomerType findCustomerType(long customerTypePk) {
        CustomerType customerType = verifiedCustomerType(customerTypePk);
        return customerType;
    }

    // UPDATE
    public CustomerType patchCustomerType(CustomerType customerType) {
        CustomerType findCustomerType = verifiedCustomerType(customerType.getPk());
        Optional.ofNullable(customerType.getType()).ifPresent(findCustomerType::setType);
        Optional.ofNullable(customerType.getDetail()).ifPresent(findCustomerType::setDetail);

        return customerTypeRespository.save(findCustomerType);
    }

    // DELETE
    public void deleteCustomerType(long customerTypePk) {
        CustomerType customerType = verifiedCustomerType(customerTypePk);
        customerTypeRespository.delete(customerType);
    }

    // 검증
    public CustomerType verifiedCustomerType(long customerTypePk) {
        Optional<CustomerType> customerType = customerTypeRespository.findById(customerTypePk);
        return customerType.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CUSTOM_TYPE_NOT_FOUND));
    }
}
