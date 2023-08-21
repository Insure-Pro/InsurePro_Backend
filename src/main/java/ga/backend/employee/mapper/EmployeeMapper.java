package ga.backend.employee.mapper;

import ga.backend.employee.dto.EmployeeRequestDto;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee employeePostDtoToEmployee(EmployeeRequestDto.Post post);
    Employee employeePatchDtoToEmployee(EmployeeRequestDto.Patch patch);
    EmployeeResponseDto.Response employeeToEmployeeResponseDto(Employee employee);
}