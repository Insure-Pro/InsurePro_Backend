package ga.backend.employee.repository;

import ga.backend.employee.entity.Employee;
import ga.backend.team.entity.Team;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Cacheable(value = "employees", key = "#email")
    @EntityGraph(attributePaths = {"company", "roles"})
    Optional<Employee> findByEmailAndDelYnFalse(String email);
    List<Employee> findFistByIdAndDelYnFalse(String id);
    Optional<Employee> findByPkAndDelYnFalse(long pk);
    Optional<Employee> findByKakaoIdAndDelYnFalse(long kakaoId);
    List<Employee> findEmployeesByTeamAndDelYnFalse(Team team);
}
