package ga.backend.ta.repository;

import ga.backend.customer.entity.Customer;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import ga.backend.ta.entity.TA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TARepository extends JpaRepository<TA, Long> {
    Optional<TA> findByPkAndDelYnFalse(Long pk);
    List<TA> findByCustomerAndDelYnFalse(Customer customer);

    // TA의 Customer 개수
    List<TA> findByEmployeeAndDateBetweenAndDelYnFalseAndCustomerCustomerTypeOrderByDateDescTimeDescPkDesc(Employee employee, LocalDate start, LocalDate end, CustomerType customerType);
}