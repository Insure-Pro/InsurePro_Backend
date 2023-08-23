package ga.backend.employee.service;

import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRespository;

    // CREATE
    public Employee createEmployee(Employee employee) {
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
        Optional.ofNullable(employee.getLoginId()).ifPresent(findEmployee::setLoginId);
        Optional.ofNullable(employee.getEmail()).ifPresent(findEmployee::setEmail);
        Optional.ofNullable(employee.getPassword()).ifPresent(findEmployee::setPassword);

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
        return employee.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMPANY_NOT_FOUND));
    }
}
