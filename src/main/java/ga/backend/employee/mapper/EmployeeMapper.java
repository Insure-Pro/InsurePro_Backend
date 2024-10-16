package ga.backend.employee.mapper;

import ga.backend.employee.dto.EmployeeRequestDto;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import ga.backend.team.dto.TeamResponseDto;
import ga.backend.team.entity.Team;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee employeePostDtoToEmployee(EmployeeRequestDto.Post post);
    Employee employeeSigninDtoToEmployee(EmployeeRequestDto.Signin signin);
    Employee employeeSigninDtoToEmployee(EmployeeRequestDto.KakaoSignin signin);
    Employee employeePatchDtoToEmployee(EmployeeRequestDto.Patch patch);
    Employee employeeChangePasswordToEmployee(EmployeeRequestDto.ChangePassword changePassword);
    EmployeeResponseDto.Response employeeToEmployeeResponseDto(Employee employee);
    EmployeeResponseDto.SimpleResponse employeeToEmployeeSimpleResponseDto(Employee employee);

    List<EmployeeResponseDto.Response> employeeToEmployeeListResponseDto(List<Employee> employees);
}