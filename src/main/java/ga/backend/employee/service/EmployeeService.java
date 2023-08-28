package ga.backend.employee.service;

import ga.backend.authorizationNumber.service.AuthorizationNumberService;
import ga.backend.company.service.CompanyService;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.event.UserRegistrationApplicationEvent;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.oauth2.utils.CustomAuthorityUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRespository;
    private final CompanyService companyService;
    private final CustomAuthorityUtils authorityUtils;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;
    private final AuthorizationNumberService authorizationNumberService;

    // CREATE
    public Employee createEmployee(Employee employee, long companyPk, int authNum) {
        employee.setRoles(authorityUtils.createRoles(employee.getEmail())); // 권한 설정
        employee.setPassword(passwordEncoder.encode(employee.getPassword())); // 비밀번호 인코딩
        if(companyPk != 0) employee.setCompany(companyService.verifiedCompany(companyPk)); // 회사 연관관계 설정
        authorizationNumberService.checkAuthNum(employee.getEmail(), authNum); // 인증번호와 이메일 확인
        publisher.publishEvent(new UserRegistrationApplicationEvent(employee));

        return employeeRespository.save(employee);
    }

    // 비밀번호 확인
    public void checkPassword(String password, String rePassword) {
        if(!password.equals(rePassword)) throw new BusinessLogicException(ExceptionCode.PASSWORD_AND_REPASSWORD_NOT_SAME);
    }

    // READ
    public Employee findEmployee(long employeePk) {
        Employee employee = verifiedEmployeeByPk(employeePk);
        return employee;
    }

    // UPDATE
    public Employee patchEmployee(Employee employee) {
        Employee findEmployee = verifiedEmployeeByPk(employee.getPk());
        Optional.ofNullable(employee.getId()).ifPresent(findEmployee::setId);
        Optional.ofNullable(employee.getEmail()).ifPresent(findEmployee::setEmail);
        Optional.ofNullable(employee.getPassword()).ifPresent(findEmployee::setPassword);
        Optional.ofNullable(employee.getAccessToken()).ifPresent(findEmployee::setAccessToken);
        Optional.ofNullable(employee.getRefreshToken()).ifPresent(findEmployee::setRefreshToken);

        return employeeRespository.save(findEmployee);
    }

    public Employee patchEmployeeToken(Employee employee) {
        Employee findEmployee = verifiedEmployeeByPk(employee.getPk());
        Optional.ofNullable(employee.getAccessToken()).ifPresent(findEmployee::setAccessToken);
        Optional.ofNullable(employee.getRefreshToken()).ifPresent(findEmployee::setRefreshToken);

        return employeeRespository.save(findEmployee);
    }

    // DELETE
    public void deleteEmployee(long employeePk) {
        Employee employee = verifiedEmployeeByPk(employeePk);
        employeeRespository.delete(employee);
    }

    // 검증 - pk
    public Employee verifiedEmployeeByPk(long employeePk) {
        Optional<Employee> employee = employeeRespository.findById(employeePk);
        return employee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
    }

    // 검증 - 이메일
    public Employee verifiedEmployeeByEmail(String email) {
        Optional<Employee> employee = employeeRespository.findByEmail(email);
        return employee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
    }

    // 검증 - 사번
    public Employee verifiedEmployeeById(String id) {
        Optional<Employee> employee = employeeRespository.findById(id);
        return employee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
    }

    // 로그인한 직원 가져오기
    public Employee getLoginEmployee() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //SecurityContextHolder에서 회원정보 가져오기
        Optional<Employee> employee = employeeRespository.findByEmail(principal.toString());

        return employee.orElse(null);
    }
}
