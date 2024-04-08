package ga.backend.customerType.service;

import ga.backend.company.entity.Company;
import ga.backend.company.service.CompanyService;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.entity.DataType;
import ga.backend.customerType.repository.CustomerTypeRepository;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.hide.entity.Hide;
import ga.backend.hide.repository.HideRepository;
import ga.backend.hide.service.HideService;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerTypeService {
    private final CustomerTypeRepository customerTypeRepository;
    private final CompanyService companyService;
    private final HideRepository hideRepository;
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

    // 회사별 고객유형 조회
    public List<CustomerType> findCustomerTypeByCompanyPk(long companyPk) {
        Company company = companyService.findCompany(companyPk);
        return customerTypeRepository.findByCompany(company);
    }

    // 고객별 고객유형 조회
    public List<CustomerType> findCustomerTypeByEmployee() {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        // 고객의 회사 조회
        Company company = employee.getCompany();

        // "회사 && delYn=false"인 고객유형 조회
        List<CustomerType> customerTypes = customerTypeRepository.findByCompanyAndDelYnFalse(company);

        // hide에 있는 customerType -> 조회에 제외되어야 하는 것
        List<CustomerType> hideCustomer = hideRepository.findByEmployee(employee).stream().map(Hide::getCustomerType).collect(Collectors.toList());

        // hide에서 조회된 customerType 제외
        customerTypes.removeAll(hideCustomer);

        return customerTypes;
    }

    // 고객유형 이름으로 고객유형 조회
    public CustomerType findCustomerTypeByName(String name) {
        Optional<CustomerType> customerTypes = customerTypeRepository.findByNameAndDelYnFalse(name);
        return customerTypes.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CUSTOMERTYPE_NOT_FOUND));
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
        Optional<CustomerType> customerTypes = customerTypeRepository.findByPkAndDelYnFalse(customerTypePk);
        return customerTypes.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CUSTOMERTYPE_NOT_FOUND));
    }

    // dataType이 DB인 경우 -> true
    public Boolean dataTypeisDB(CustomerType customerType) {
        return customerType.getDataType() == DataType.DB;
    }

    // dataType이 ETC인 경우 -> true
    public Boolean dataTypeisETC(CustomerType customerType) {
        return customerType.getDataType() == DataType.ETC;
    }
}
