package ga.backend.customer.repository;

import ga.backend.customer.entity.Customer;
import ga.backend.employee.entity.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // CustomerService
    Optional<Customer> findByPkAndDelYnFalse(long customerPk);

    // 최신순
    List<Customer> findByEmployeeAndDelYnFalse(Employee employee, Sort sort);
    List<Customer> findAllByEmployeeAndCreatedAtBetweenAndCustomerTypeIn(Employee employee, Sort sort, LocalDateTime start, LocalDateTime finish, List<Customer.CustomerType> customerTypes);
    List<Customer> findAllByEmployeeAndRegisterDateBetweenAndCustomerTypeIn(Employee employee, Sort sort, LocalDate start, LocalDate finish, List<Customer.CustomerType> customerTypes);

    // 나이별
    List<Customer> findByEmployeeAndAgeBetweenAndDelYnFalse(Employee employee, int start, int end, Sort sort);
    List<Customer> findByEmployeeAndAgeBetweenAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, int startAge, int endAge, Sort sort, LocalDateTime start, LocalDateTime finish, List<Customer.CustomerType> customerTypes);
    List<Customer> findByEmployeeAndAgeBetweenAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, int startAge, int endAge, Sort sort, LocalDate start, LocalDate finish, List<Customer.CustomerType> customerTypes);

    // 지역별
    List<Customer> findByEmployeeAndDongStringContainsAndDelYnFalse(Employee employee, String dongName, Sort sort);
    List<Customer> findByEmployeeAndDongStringContainsAndCreatedAtBetweenAndDelYnFalse(Employee employee, String dongName, Sort sort, LocalDateTime start, LocalDateTime finish, List<Customer.CustomerType> customerTypes);
    List<Customer> findByEmployeeAndDongStringContainsAndRegisterDateBetweenAndDelYnFalse(Employee employee, String dongName, Sort sort, LocalDate start, LocalDate finish, List<Customer.CustomerType> customerTypes);

    // 계약 완려 여부
    List<Customer> findByEmployeeAndContractYnAndDelYnFalse(Employee employee, boolean contractYn, Sort sort);
    List<Customer> findByEmployeeAndContractYnAndCreatedAtBetweenAndDelYnFalse(Employee employee, boolean contractYn, LocalDateTime start, LocalDateTime finish);

    // 이름 검색
    List<Customer> findByEmployeeAndName(Employee employee, String name);

    // -----------------------------------------------------------------------------
    // 성과분석(analysis) RegisterDate
    // all 계산
    List<Customer> findByEmployeeAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(
            Employee employee,
            LocalDate createdAtStart,
            LocalDate createdAtFinish,
            Customer.CustomerType customerType
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
