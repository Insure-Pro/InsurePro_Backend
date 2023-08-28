package ga.backend.employee.controller;

import ga.backend.employee.dto.EmployeeRequestDto;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.mapper.EmployeeMapper;
import ga.backend.employee.service.EmployeeService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl + "/employee")
@Validated
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    // CREATE - 회원가입
    @PostMapping("/signin")
    public ResponseEntity postEmployee(@Valid @RequestBody EmployeeRequestDto.Signin signin) {
        employeeService.checkPassword(signin.getPassword(), signin.getRePassword()); // 비밀번호 확인
        Employee employee = employeeService.createEmployee(
                employeeMapper.employeeSigninDtoToEmployee(signin),
                signin.getCompanyPk(),
                signin.getAuthNum()
        );
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{employee-pk}")
    public ResponseEntity getEmployee(@Positive @PathVariable("employee-pk") long employeePk) {
        Employee employee = employeeService.findEmployee(employeePk);
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 가입한 이메일 찾기
    @GetMapping
    public ResponseEntity getEmployee(@RequestParam("id") String employee_id) {
        Employee employee = employeeService.verifiedEmployeeById(employee_id);
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchEmployee(@Valid @RequestBody EmployeeRequestDto.Patch patch) {
        Employee employee = employeeService.patchEmployee(employeeMapper.employeePatchDtoToEmployee(patch));
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{employee-pk}")
    public ResponseEntity deleteEmployee(@Positive @PathVariable("employee-pk") long employeePk) {
        employeeService.deleteEmployee(employeePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
