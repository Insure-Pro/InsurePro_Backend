package ga.backend.customer.repository;

import ga.backend.customer.entity.Customer;
import ga.backend.employee.entity.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByEmployee(Employee employee, Sort sort);

    List<Customer> findByEmployeeAndAgeBetween(Employee employee, int start, int end, Sort sort);

    List<Customer> findByEmployeeAndDongStringContains(Employee employee, String dongName, Sort sort);
    List<Customer> findByEmployeeAndContractYn(Employee employee, boolean contractYn);

    @Query("SELECT c FROM Customer c " +
            "WHERE (c.employee = :employee AND (c.intensiveCareStartDate != NULL OR c.intensiveCareFinishDate != NULL))")
    List<Customer> findByEmployeeAndIntensiveCareExists(Employee employee);
}
