package ga.backend.util;

import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@AllArgsConstructor
public class FindEmployee {
    private final EmployeeRepository employeeRepository;

    // 로그인한 직원 가져오기
    public Employee getLoginEmployeeByToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //SecurityContextHolder에서 회원정보 가져오기
        Optional<Employee> employee = employeeRepository.findByEmail(principal.toString());

//        return employee.orElse(null);
        return employee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
    }
}