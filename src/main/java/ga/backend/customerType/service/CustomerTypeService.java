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
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    private final List<String> CustomerTypeColors = new ArrayList<>(List.of(
            "#e70000",
            "#fa7533",
            "#21a549",
            "#1060ff",
            "#7543ff",
            "#bb5abb",
            "#ff8a8a",
            "#e5ba00",
            "#91b6ff"
    ));
    private final String CustomerTypeFirstColor = CustomerTypeColors.get(0);
    private final int CustomerTypeColorSize = CustomerTypeColors.size();

    // CREATE
    @Transactional
    public CustomerType createCustomerType(CustomerType customerType) {
        customerType.setName(customerType.getName().toUpperCase()); // 고객유형 이름은 무조건 대문자만 허용

        // NULL 이름의 default customerType 생성 불가능
        if(customerType.getName().equals("NULL")) {
            throw new BusinessLogicException(ExceptionCode.CUSTOMER_TYPE_NAME_NULL);
        }

        Employee employee = findEmployee.getLoginEmployeeByToken();
        customerType.setEmployeePk(employee.getPk());
        customerType.setCompany(employee.getCompany());

        // 색상 자동추가 -> 색상값이 없는 경우
        if(customerType.getColor() == null) {
            Company company = employee.getCompany();
            List<CustomerType> customerTypes = customerTypeRepository.findByCompanyAndDelYnFalse(company);
            String lastColor = customerTypes.get(customerTypes.size()-1).getColor(); // 마지막에 설정한 색상
            int index = CustomerTypeColors.indexOf(lastColor); // 지정한 컬러의 index 확인

            // 지정된 컬러 이외의 색상일 경우 or 마지막 컬러인 경우
            if(index == -1 || index == CustomerTypeColorSize-1) customerType.setColor(CustomerTypeFirstColor);
            else customerType.setColor(CustomerTypeColors.get(index+1));
        }

        return customerTypeRepository.save(customerType);
    }

    // READ
    public CustomerType findCustomerType(long customerTypePk) {
        return verifiedCustomerType(customerTypePk);
    }

    // 회사별 고객유형 조회 by companyPk
    public List<CustomerType> findCustomerTypeByCompanyPk(long companyPk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        Company company = companyService.findCompany(companyPk);

        if(employee.getCompany() != company) throw new BusinessLogicException(ExceptionCode.EMPOYEE_AND_COMPANY_NOT_MATCH);

        return customerTypeRepository.findByCompanyAndDelYnFalse(company);
    }

    // 회사별 고객유형 조회 by Employee
    public List<CustomerType> findCustomerTypeByCompanyFromEmployee(Employee employee) {
        return customerTypeRepository.findByCompanyAndDelYnFalse(employee.getCompany());
    }

    // 고객별 고객유형 조회
    public List<CustomerType> findCustomerTypeByEmployee() {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        // "회사 && delYn=false"인 고객유형 조회
        List<CustomerType> customerTypes = findCustomerTypeByCompanyFromEmployee(employee);

        // hide에 있는 customerType -> 조회에 제외되어야 하는 것
        List<CustomerType> hideCustomer = findCustomerTypeByHide(employee);

        // hide에서 조회된 customerType 제외
        customerTypes.removeAll(hideCustomer);

        return customerTypes;
    }

    // 숨기기한 고객유형 목록 조회
    public List<CustomerType> findCustomerTypeByHide() {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        return findCustomerTypeByHide(employee);
    }

    // hide에 있는 customerType
    public List<CustomerType> findCustomerTypeByHide(Employee employee) {
        return hideRepository.findByEmployee(employee).stream().map(Hide::getCustomerType).collect(Collectors.toList());
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
        Optional.ofNullable(customerType.getAsSetting()).ifPresent(findCustomerType::setAsSetting);
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
