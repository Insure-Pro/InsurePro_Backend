package ga.backend.employee.service;

import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.event.UserRegistrationApplicationEvent;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.oauth2.utils.CustomAuthorityUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRespository;
    private final CustomAuthorityUtils authorityUtils;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    // CREATE
    public Employee createEmployee(Employee employee) {
        employee.setRoles(authorityUtils.createRoles(employee.getEmail())); // 권한 설정
        employee.setPassword(passwordEncoder.encode(employee.getPassword())); // 비밀번호 인코딩
        publisher.publishEvent(new UserRegistrationApplicationEvent(employee));

        return employeeRespository.save(employee);
    }

    // READ
    public Employee findEmployee(long employeePk) {
        Employee employee = verifiedEmployee(employeePk);
        return employee;
    }

    // UPDATE
    public Employee patchEmployee(Employee employee) {
        Employee findEmployee = verifiedEmployee(employee.getPk());
        Optional.ofNullable(employee.getId()).ifPresent(findEmployee::setId);
        Optional.ofNullable(employee.getEmail()).ifPresent(findEmployee::setEmail);
        Optional.ofNullable(employee.getPassword()).ifPresent(findEmployee::setPassword);
        Optional.ofNullable(employee.getAccessToken()).ifPresent(findEmployee::setAccessToken);
        Optional.ofNullable(employee.getRefreshToken()).ifPresent(findEmployee::setRefreshToken);

        return employeeRespository.save(findEmployee);
    }

    // DELETE
    public void deleteEmployee(long employeePk) {
        Employee employee = verifiedEmployee(employeePk);
        employeeRespository.delete(employee);
    }

    // 검증
    public Employee verifiedEmployee(long employeePk) {
        Optional<Employee> employee = employeeRespository.findById(employeePk);
        return employee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
    }

    // 로그인한 직원 가져오기
    public Employee getLoginEmployee() {

    }
}
