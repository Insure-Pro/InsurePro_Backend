package ga.backend.employee.repository;

import ga.backend.employee.entity.Employee;
import ga.backend.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmailAndDelYnFalse(String email);
    List<Employee> findFistByIdAndDelYnFalse(String id);
    Optional<Employee> findByPkAndDelYnFalse(long pk);
    List<Employee> findEmployeesByTeamAndDelYnFalse(Team team);
}
