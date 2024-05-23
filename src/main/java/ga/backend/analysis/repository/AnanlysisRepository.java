package ga.backend.analysis.repository;

import ga.backend.analysis.entity.Analysis;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AnanlysisRepository extends JpaRepository<Analysis, Long> {
    Optional<Analysis> findByEmployeeAndDateAndCustomerType(Employee employee, LocalDate localDate, CustomerType customerType);
}
