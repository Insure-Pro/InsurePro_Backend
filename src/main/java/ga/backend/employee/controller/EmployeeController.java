package ga.backend.employee.controller;

import ga.backend.employee.dto.EmployeeRequestDto;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.mapper.EmployeeMapper;
import ga.backend.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/employee")
@Validated
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postEmployee(@Valid @RequestBody EmployeeRequestDto.Post post) {
        Employee employee = employeeService.createEmployee(employeeMapper.employeePostDtoToEmployee(post));
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{employee-pk}")
    public ResponseEntity getEmployee(@Positive @PathVariable("employee-pk") long employeePk) {
        Employee employee = employeeService.findEmployee(employeePk);
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchEmployee(@Valid @RequestBody EmployeeRequestDto.Patch patch) {
        Employee employee = employeeService.patchEmployee(employeeMapper.employeePatchDtoToEmployee(patch));
        EmployeeResponseDto.Response response = employeeMapper.employeeToEmployeeResponseDto(employee);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // DELETE
    @DeleteMapping("/{employee-pk}")
    public ResponseEntity deleteEmployee(@Positive @PathVariable("employee-pk") long employeePk) {
        employeeService.deleteEmployee(employeePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}