package ga.backend.customer.repository;

import ga.backend.customer.entity.Customer;
import ga.backend.employee.entity.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // CustomerService
    List<Customer> findAllByEmployee(Employee employee, Sort sort);
    List<Customer> findAllByEmployeeAndCreatedAtBetween(Employee employee, Sort sort, LocalDateTime start, LocalDateTime finish);

    List<Customer> findByEmployeeAndAgeBetween(Employee employee, int start, int end, Sort sort);

    List<Customer> findByEmployeeAndDongStringContains(Employee employee, String dongName, Sort sort);
    List<Customer> findByEmployeeAndContractYn(Employee employee, boolean contractYn);
    List<Customer> findByEmployeeAndName(Employee employee, String name);

    // -----------------------------------------------------------------------------
    // 성과분석(analysis)

    // all 계산
    List<Customer> findByEmployeeAndCreatedAtBetween(
            Employee employee,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtFinish
    );

    // 성과분석 확인(다시 계산)
//    List<Customer> findByEmployeeAndModifiedAtGreaterThanEqual(
//            Employee employee,
//            LocalDateTime modifiedAfter
//    );

    @Query("SELECT c FROM Customer c " +
            "WHERE (c.employee = :employee AND (c.createdAt >= :createdAtAfter OR c.modifiedAt >= :modifiedAfter))")
    List<Customer> findByEmployeeAndCreatedAtGreaterThanEqualOrModifiedAtGreaterThanEqual(
            Employee employee,
            LocalDateTime createdAtAfter,
            LocalDateTime modifiedAfter
    );
}
