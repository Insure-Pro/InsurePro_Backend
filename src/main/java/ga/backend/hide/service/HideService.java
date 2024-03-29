package ga.backend.hide.service;

import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.service.CustomerTypeService;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.hide.entity.Hide;
import ga.backend.hide.repository.HideRepository;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HideService {
    private final HideRepository hideRepository;
    private final FindEmployee findCompany;
    private final CustomerTypeService customerTypeService;

    // CREATE
    public Hide createHide(Hide hide) {
        hide.setEmployee(findCompany.getLoginEmployeeByToken());
        return hideRepository.save(hide);
    }

    public Hide createHide(Long customerTypePk) {
        Employee employee = findCompany.getLoginEmployeeByToken();
        CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

        // customerType이 employee's company에 속하는지 확인
        if(!Objects.equals(employee.getCompany().getPk(), customerType.getCompany().getPk())) {
            throw new BusinessLogicException(ExceptionCode.EMPLOYEE_AND_CUSTOMERTYPE_NOT_MATCH);
        }

        Hide hide = new Hide();
        hide.setEmployee(employee);
        hide.setCustomerType(customerType);
        return hideRepository.save(hide);
    }

    // READ
    public Hide findHide(long hidePk) {
        return verifiedHide(hidePk);
    }

    // DELETE
    public void deleteHide(long customerTypePk) {
        Employee employee = findCompany.getLoginEmployeeByToken();
        CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

        Optional<Hide> hide = hideRepository.findByEmployeeAndCustomerType(employee, customerType);
        hide.ifPresent(hideRepository::delete);
    }

    // 검증
    public Hide verifiedHide(long hidePk) {
        Optional<Hide> hide = hideRepository.findById(hidePk);
        return hide.orElseThrow(() -> new BusinessLogicException(ExceptionCode.HIDE_NOT_FOUND));
    }
}