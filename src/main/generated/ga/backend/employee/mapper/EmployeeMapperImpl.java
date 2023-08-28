package ga.backend.employee.mapper;

import ga.backend.employee.dto.EmployeeRequestDto;
import ga.backend.employee.dto.EmployeeResponseDto;
import ga.backend.employee.entity.Employee;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-28T17:13:29+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public Employee employeePostDtoToEmployee(EmployeeRequestDto.Post post) {
        if ( post == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setPk( post.getPk() );
        employee.setId( post.getId() );
        employee.setEmail( post.getEmail() );
        employee.setPassword( post.getPassword() );
        employee.setRegiYn( post.isRegiYn() );
        employee.setDelYn( post.isDelYn() );

        return employee;
    }

    @Override
    public Employee employeeSigninDtoToEmployee(EmployeeRequestDto.Signin signin) {
        if ( signin == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setId( signin.getId() );
        employee.setEmail( signin.getEmail() );
        employee.setPassword( signin.getPassword() );

        return employee;
    }

    @Override
    public Employee employeePatchDtoToEmployee(EmployeeRequestDto.Patch patch) {
        if ( patch == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setPk( patch.getPk() );
        employee.setId( patch.getId() );
        employee.setEmail( patch.getEmail() );
        employee.setPassword( patch.getPassword() );
        employee.setRegiYn( patch.isRegiYn() );
        employee.setDelYn( patch.isDelYn() );

        return employee;
    }

    @Override
    public EmployeeResponseDto.Response employeeToEmployeeResponseDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        Long pk = null;
        String id = null;
        String email = null;
        boolean regiYn = false;
        boolean delYn = false;
        LocalDateTime createdAt = null;
        LocalDateTime modifiedAt = null;

        pk = employee.getPk();
        id = employee.getId();
        email = employee.getEmail();
        regiYn = employee.isRegiYn();
        delYn = employee.isDelYn();
        createdAt = employee.getCreatedAt();
        modifiedAt = employee.getModifiedAt();

        EmployeeResponseDto.Response response = new EmployeeResponseDto.Response( pk, id, email, regiYn, delYn, createdAt, modifiedAt );

        return response;
    }
}
