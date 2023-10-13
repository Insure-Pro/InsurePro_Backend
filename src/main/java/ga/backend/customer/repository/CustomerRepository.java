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

    List<Customer> findByEmployeeAndAgeBetween(Employee employee, int startAge, int endAge, Sort sort);
    List<Customer> findByEmployeeAndAgeBetweenAndCreatedAtBetween(Employee employee, int startAge, int endAge, Sort sort, LocalDateTime start, LocalDateTime finish);

    List<Customer> findByEmployeeAndDongStringContains(Employee employee, String dongName, Sort sort);
    List<Customer> findByEmployeeAndDongStringContainsAndCreatedAtBetween(Employee employee, String dongName, Sort sort, LocalDateTime start, LocalDateTime finish);
    List<Customer> findByEmployeeAndContractYn(Employee employee, boolean contractYn);
    List<Customer> findByEmployeeAndContractYnAndCreatedAtBetween(Employee employee, boolean contractYn,  LocalDateTime start, LocalDateTime finish);
    List<Customer> findByEmployeeAndName(Employee employee, String name);

    // -----------------------------------------------------------------------------
    // 성과분석(analysis)
    Optional<Customer> findByPkAndDelYnFalse(long customerPk);

    List<Customer> findByEmployeeAndDelYnFalse(Employee employee, Sort sort);

    List<Customer> findByEmployeeAndAgeBetweenAndDelYnFalse(Employee employee, int start, int end, Sort sort);

    List<Customer> findByEmployeeAndDongStringContainsAndDelYnFalse(Employee employee, String dongName, Sort sort);

    List<Customer> findByEmployeeAndContractYnAndDelYnFalse(Employee employee, boolean contractYn);

    @Query("SELECT c FROM Customer c " +
            "WHERE (c.employee = :employee AND c.delYn = false AND (c.intensiveCareStartDate != NULL OR c.intensiveCareFinishDate != NULL))")
    List<Customer> findByEmployeeAndIntensiveCareExists(Employee employee);

    // all 계산
    List<Customer> findByEmployeeAndCreatedAtBetweenAndDelYnFalse(
            Employee employee,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtFinish
    );

    // 성과분석 확인(다시 계산할지 여부 확인)
    @Query("SELECT c FROM Customer c " +
            "WHERE (c.employee = :employee AND (c.createdAt >= :createdAtAfter OR c.modifiedAt >= :modifiedAfter))")
    List<Customer> findByEmployeeAndCreatedAtGreaterThanEqualOrModifiedAtGreaterThanEqual(
            Employee employee,
            LocalDateTime createdAtAfter,
            LocalDateTime modifiedAfter
    );
}
