package ga.backend.employee.repository;

import ga.backend.employee.entity.Employee;
import ga.backend.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findById(String id);
    Optional<Employee> findById(long pk);
    List<Employee> findEmployeesByTeam(Team team);
}
