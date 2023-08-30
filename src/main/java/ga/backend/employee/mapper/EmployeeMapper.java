package ga.backend.employee.mapper;

import ga.backend.employee.dto.EmployeeRequestDto;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee employeePostDtoToEmployee(EmployeeRequestDto.Post post);
    Employee employeeSigninDtoToEmployee(EmployeeRequestDto.Signin signin);
    Employee employeePatchDtoToEmployee(EmployeeRequestDto.Patch patch);
    Employee employeeChangePasswordToEmployee(EmployeeRequestDto.ChangePassword changePassword);
    EmployeeResponseDto.Response employeeToEmployeeResponseDto(Employee employee);
}