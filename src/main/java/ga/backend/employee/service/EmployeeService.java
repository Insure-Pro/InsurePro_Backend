package ga.backend.employee.service;

import ga.backend.authorizationNumber.service.AuthorizationNumberService;
import ga.backend.company.entity.Company;
import ga.backend.company.service.CompanyService;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.event.UserRegistrationApplicationEvent;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.oauth2.utils.CustomAuthorityUtils;
import ga.backend.team.service.TeamService;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRespository;
    private final CompanyService companyService;
    private final TeamService teamService;
    private final CustomAuthorityUtils authorityUtils;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;
    private final AuthorizationNumberService authorizationNumberService;
    private final FindEmployee findEmployee;

    // CREATE
    public Employee createEmployee(Employee employee, Long companyPk, String companyName, long teamPk, int authNum) {
        authorizationNumberService.checkAuthNum(employee.getEmail(), authNum); // 인증번호와 이메일 확인
        employee.setRoles(authorityUtils.createRoles(employee.getEmail())); // 권한 설정
        employee.setPassword(passwordEncoder.encode(employee.getPassword())); // 비밀번호 인코딩
        publisher.publishEvent(new UserRegistrationApplicationEvent(employee)); //  사용자 등록 이벤트를 발생시키는 코드

        // 팀 연관관계 설정
        setTeamAndCompany(employee, companyPk, companyName, teamPk);

        return employeeRespository.save(employee);
    }

    // CREATE - 카카오 소셜
    public Employee createKakaoEmployee(Employee employee, Long companyPk, String companyName, long teamPk) {
        // 카카오톡 ID 필수값
        if(employee.getKakaoId() == null) throw new BusinessLogicException(ExceptionCode.KAKAO_ID_NOT_FOUND);

        // 이미 가입된 회원인지 확인
        Optional<Employee> findEmployee = employeeRespository.findByKakaoIdAndDelYnFalse(employee.getKakaoId());
        if(findEmployee.isPresent()) throw new BusinessLogicException(ExceptionCode.DUPLICATE_KAKAO_ID);

        // 같은 이메일로 가입된 회원이 있는 경우 -> 카카오톡 로그인 추가하기
        Optional<Employee> findEmailEmployeeOptional = employeeRespository.findByEmailAndDelYnFalse(employee.getEmail());
        if(findEmailEmployeeOptional.isPresent() && findEmailEmployeeOptional.get().getKakaoId() == null) {
            Employee findEmailEmployee = findEmailEmployeeOptional.get();
            findEmailEmployee.setKakaoId(employee.getKakaoId());
            findEmailEmployee.setKakaoPw(passwordEncoder.encode(String.valueOf(employee.getKakaoId()))); // 카카오톡 ID를 이용해서 비밀번호 인코딩
            return employeeRespository.save(findEmailEmployee);
        }

        employee.setRoles(authorityUtils.createRoles(employee.getEmail())); // 권한 설정
        employee.setKakaoPw(passwordEncoder.encode(String.valueOf(employee.getKakaoId()))); // 카카오톡 ID를 이용해서 비밀번호 인코딩
        publisher.publishEvent(new UserRegistrationApplicationEvent(employee)); //  사용자 등록 이벤트를 발생시키는 코드

        // 팀 연관관계 설정
        setTeamAndCompany(employee, companyPk, companyName, teamPk);

        return employeeRespository.save(employee);
    }

    // 회사와 팀 연관관계 형성
    public void setTeamAndCompany(Employee employee, Long companyPk, String companyName, long teamPk) {
        // 팀 연관관계 설정
        if(teamPk != 0) employee.setTeam(teamService.verifiedTeam(teamPk));

        // 회사 연관관계 설정
        if(companyPk != null) employee.setCompany(companyService.verifiedCompany(companyPk));
        else if(companyName == null || companyName.equals("")) { // 회사PK와 회사이름이 없으면 자동으로 이름 생성
            // 새로 회사를 생성
            Company company = new Company();
            String autoCompanyName = "GA_" + employee.getEmail().split("@")[0];
            company.setName(autoCompanyName);
            company = companyService.createCompany(company);
            employee.setCompany(company);
        } else { // 사용자 설정으로 새로운 회사 생성하기
            // 기존에 같은 회사가 있는지 탐색
            Optional<Company> findCompany = companyService.findCompanyByName(companyName);
            if(findCompany.isPresent()) employee.setCompany(findCompany.get());
            else { // 새로 회사를 생성
                Company company = new Company();
                company.setName(companyName);
                company = companyService.createCompany(company);
                employee.setCompany(company);
            }
        }
    }

    // 비밀번호 확인
    public void checkPassword(String password, String rePassword) {
        if(!password.equals(rePassword)) throw new BusinessLogicException(ExceptionCode.PASSWORD_AND_REPASSWORD_NOT_SAME);
    }

    // READ
    public Employee findEmployeeByPk(long employeePk) {
        Employee employee = verifiedEmployeeByPk(employeePk);
        return employee;
    }

    // 토큰으로 직원 조회
    public Employee findEmployeeByToken() {
        Employee employee = findEmployee.getLoginEmployeeByToken();

        return employee;
    }

    // UPDATE
    public Employee patchEmployee(Employee employee) {
        Employee findEmployee = findEmployeeByToken();
        Optional.ofNullable(employee.getId()).ifPresent(findEmployee::setId);
        Optional.ofNullable(employee.getYear()).ifPresent(findEmployee::setYear);
        Optional.ofNullable(employee.getEmail()).ifPresent(findEmployee::setEmail);
        Optional.ofNullable(employee.getName()).ifPresent(findEmployee::setName);
        Optional.ofNullable(employee.getDelYn()).ifPresent(findEmployee::setDelYn);
        Optional.ofNullable(employee.getRegiYn()).ifPresent(findEmployee::setRegiYn);
        Optional.ofNullable(employee.getRegiYn()).ifPresent(findEmployee::setRegiYn);

        return employeeRespository.save(findEmployee);
    }

    // 로그아웃 시 토큰값 변경
    public Employee patchEmployeeToken(Employee employee) {
        Employee findEmployee = verifiedEmployeeByPk(employee.getPk());
        Optional.ofNullable(employee.getAccessToken()).ifPresent(findEmployee::setAccessToken);
        Optional.ofNullable(employee.getRefreshToken()).ifPresent(findEmployee::setRefreshToken);

        return employeeRespository.save(findEmployee);
    }

    // 비밀번호 변경
    public Employee changePassword(Employee employee, int authNum) {
        Employee changeEmployee = verifiedEmployeeByEmail(employee.getEmail());
        changeEmployee.setPassword(passwordEncoder.encode(employee.getPassword())); // 비밀번호 인코딩
        authorizationNumberService.checkAuthNum(changeEmployee.getEmail(), authNum); // 인증번호와 이메일 확인

        publisher.publishEvent(new UserRegistrationApplicationEvent(changeEmployee));

        return employeeRespository.save(changeEmployee);
    }

    // DELETE
    public void deleteEmployee(long employeePk) {
        Employee employee = verifiedEmployeeByPk(employeePk);
        employeeRespository.delete(employee);
    }

    // 검증 - pk
    public Employee verifiedEmployeeByPk(long employeePk) {
         Optional<Employee> employee = employeeRespository.findByPkAndDelYnFalse(employeePk);
        return employee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
    }

    // 검증 - 이메일
    public Employee verifiedEmployeeByEmail(String email) {
        Optional<Employee> employee = employeeRespository.findByEmailAndDelYnFalse(email);
        return employee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
    }

    // 검증 - 사번
    public Employee verifiedEmployeeById(String id) {
        List<Employee> employee = employeeRespository.findFistByIdAndDelYnFalse(id);
        if(employee.isEmpty()) throw new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND);
        return employee.get(0);
    }
}
