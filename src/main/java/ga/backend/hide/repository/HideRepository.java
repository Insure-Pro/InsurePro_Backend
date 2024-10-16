package ga.backend.hide.repository;

import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import ga.backend.hide.entity.Hide;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HideRepository extends JpaRepository<Hide, Long> {
    Optional<Hide> findByEmployeeAndCustomerType(Employee employee, CustomerType customerType);
    @Cacheable(value = "hides", key = "#employee.pk")
    List<Hide> findByEmployee(Employee employee);
}
