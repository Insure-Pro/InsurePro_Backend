package ga.backend.employee.controller;

import ga.backend.employee.dto.EmployeeRequestDto;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.mapper.EmployeeMapper;
import ga.backend.employee.service.EmployeeService;
import ga.backend.oauth2.filter.JwtVerificationFilter;
import ga.backend.team.dto.TeamResponseDto;
import ga.backend.team.mapper.TeamMapper;
import ga.backend.team.repository.TeamRepository;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl + "/employee")
@Validated
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final TeamMapper teamMapper;
    private final JwtVerificationFilter jwtVerificationFilter;

    // CREATE - 회원가입
    @PostMapping("/signin")
    public ResponseEntity postEmployee(@Valid @RequestBody EmployeeRequestDto.Signin signin) {
        employeeService.checkPassword(signin.getPassword(), signin.getRePassword()); // 비밀번호 확인
        Employee employee = employeeService.createEmployee(
                employeeMapper.employeeSigninDtoToEmployee(signin),
                signin.getCompanyPk(),
                signin.getCompanyName(),
                signin.getTeamPk(),
                signin.getAuthNum()
        );
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // CREATE - 카카오 소셜 회원가입
    @PostMapping("/kakao-signin")
    public ResponseEntity postKakaoEmployee(@Valid @RequestBody EmployeeRequestDto.KakaoSignin signin) {
        Employee employee = employeeService.createKakaoEmployee(
                employeeMapper.employeeSigninDtoToEmployee(signin),
                signin.getCompanyPk(),
                signin.getCompanyName(),
                signin.getTeamPk()
        );
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{employee-pk}")
    public ResponseEntity getEmployee(@Positive @PathVariable("employee-pk") long employeePk) {
        Employee employee = employeeService.findEmployeeByPk(employeePk);
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);
        TeamResponseDto.Response teamResponse = teamMapper.teamToTeamResponseDto(employee.getTeam(), employee.getCompany().getPk());
        response.setTeamResponseDto(teamResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 토큰으로 employee 정보 조회하기
    @GetMapping
//    @Cacheable(value = "employeeCache")
    public ResponseEntity getEmployee() {
        Employee employee = employeeService.findEmployeeByToken();
        EmployeeResponseDto.SimpleResponse response = employeeMapper.employeeToEmployeeSimpleResponseDto(employee);

        // ResponseDto.Response 반환
//        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeSimpleResponseDto(employee);
//        if(employee.getTeam() != null) {
//            TeamResponseDto.Response teamResponse = teamMapper.teamToTeamResponseDto(employee.getTeam(), employee.getCompany().getPk());
//            response.setTeamResponseDto(teamResponse);
//        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // access-token으로 refresh-token 재발행
    @PatchMapping("/authorization")
    public ResponseEntity patchToken(HttpServletRequest request, HttpServletResponse response) {
//        jwtVerificationFilter.assignAccessToken(request, response);
        jwtVerificationFilter.verifyRefreshToken(request, response);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 가입한 이메일 찾기
    @GetMapping("/email")
    public ResponseEntity getEmployee(@RequestParam("id") String employee_id) {
        Employee employee = employeeService.verifiedEmployeeById(employee_id);
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);
        TeamResponseDto.Response teamResponse = teamMapper.teamToTeamResponseDto(employee.getTeam(), employee.getCompany().getPk());
        response.setTeamResponseDto(teamResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchEmployee(@Valid @RequestBody EmployeeRequestDto.Patch patch) {
        Employee employee = employeeService.patchEmployee(employeeMapper.employeePatchDtoToEmployee(patch));
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);
        TeamResponseDto.Response teamResponse = teamMapper.teamToTeamResponseDto(employee.getTeam(), employee.getCompany().getPk());
        response.setTeamResponseDto(teamResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity patchEmployee(@Valid @RequestBody EmployeeRequestDto.ChangePassword changePassword) {
        employeeService.checkPassword(changePassword.getPassword(), changePassword.getRePassword()); // 비밀번호 확인
        Employee employee = employeeService.changePassword(employeeMapper.employeeChangePasswordToEmployee(changePassword), changePassword.getAuthNum());
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);
        TeamResponseDto.Response teamResponse = teamMapper.teamToTeamResponseDto(employee.getTeam(), employee.getCompany().getPk());
        response.setTeamResponseDto(teamResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{employee-pk}")
    public ResponseEntity deleteEmployee(@Positive @PathVariable("employee-pk") long employeePk) {
        employeeService.deleteEmployee(employeePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
