package ga.backend.event;

import ga.backend.employee.entity.Employee;
import lombok.Getter;

@Getter
public class UserRegistrationApplicationEvent {
    private Employee employee;
    public UserRegistrationApplicationEvent(Employee employee) {
        this.employee = employee;
    }
}
