package ga.backend.customer.repository;

import ga.backend.customer.entity.Customer;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.entity.DataType;
import ga.backend.employee.entity.Employee;
import ga.backend.customer.entity.ConsultationStatus;
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
    List<Customer> findByEmployeeAndCustomerTypeAndDelYnFalse(Employee employee, CustomerType customerType, Sort sort);
    List<Customer> findAllByEmployeeAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, Sort sort, LocalDate start, LocalDate finish, List<CustomerType> customerTypes);
    List<Customer> findAllByEmployeeAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(Employee employee, Sort sort, LocalDate start, LocalDate finish, CustomerType customerType);
    List<Customer> findAllByEmployeeAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, Sort sort, LocalDateTime start, LocalDateTime finish, List<CustomerType> customerTypes);
    List<Customer> findAllByEmployeeAndCreatedAtBetweenAndCustomerTypeAndDelYnFalse(Employee employee, Sort sort, LocalDateTime start, LocalDateTime finish, CustomerType customerType);

    // 나이별
    List<Customer> findByEmployeeAndAgeBetweenAndDelYnFalseOrderByAge(Employee employee, int start, int end, Sort sort);
    List<Customer> findByEmployeeAndAgeBetweenAndCustomerTypeAndDelYnFalseOrderByAge(Employee employee, int start, int end, CustomerType customerType, Sort sort);
    List<Customer> findByEmployeeAndAgeBetweenAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalseOrderByAge(Employee employee, int startAge, int endAge, Sort sort, LocalDate start, LocalDate finish, List<CustomerType> customerTypes);
    List<Customer> findByEmployeeAndAgeBetweenAndRegisterDateBetweenAndCustomerTypeAndDelYnFalseOrderByAge(Employee employee, int startAge, int endAge, Sort sort, LocalDate start, LocalDate finish, CustomerType customerType);
    List<Customer> findByEmployeeAndAgeBetweenAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalseOrderByAge(Employee employee, int startAge, int endAge, Sort sort, LocalDateTime start, LocalDateTime finish, List<CustomerType> customerTypes);
    List<Customer> findByEmployeeAndAgeBetweenAndCreatedAtBetweenAndCustomerTypeAndDelYnFalseOrderByAge(Employee employee, int startAge, int endAge, Sort sort, LocalDateTime start, LocalDateTime finish, CustomerType customerType);

    // 지역별
    List<Customer> findByEmployeeAndDongStringContainsAndDelYnFalse(Employee employee, String dongName, Sort sort);
    List<Customer> findByEmployeeAndDongStringContainsAndCustomerTypeAndDelYnFalse(Employee employee, String dongName, CustomerType customerType, Sort sort);
    List<Customer> findByEmployeeAndDongStringContainsAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, String dongName, Sort sort, LocalDate start, LocalDate finish, List<CustomerType> customerTypes);
    List<Customer> findByEmployeeAndDongStringContainsAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(Employee employee, String dongName, Sort sort, LocalDate start, LocalDate finish, CustomerType customerType);
    List<Customer> findByEmployeeAndDongStringContainsAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, String dongName, Sort sort, LocalDateTime start, LocalDateTime finish, List<CustomerType> customerTypes);
    List<Customer> findByEmployeeAndDongStringContainsAndCreatedAtBetweenAndCustomerTypeAndDelYnFalse(Employee employee, String dongName, Sort sort, LocalDateTime start, LocalDateTime finish, CustomerType customerType);

    // 계약 완료 여부
    List<Customer> findByEmployeeAndContractYnAndDelYnFalse(Employee employee, boolean contractYn, Sort sort);
    List<Customer> findByEmployeeAndContractYnAndCustomerTypeAndDelYnFalse(Employee employee, boolean contractYn, CustomerType customerType, Sort sort);
    List<Customer> findByEmployeeAndContractYnAndAgeBetweenAndDelYnFalseOrderByAge(Employee employee, boolean contractYn, Sort sort, int startAge, int endAge);
    List<Customer> findByEmployeeAndContractYnAndAgeBetweenAndCustomerTypeAndDelYnFalseOrderByAge(Employee employee, boolean contractYn, Sort sort, int startAge, int endAge, CustomerType customerType);
    List<Customer> findByEmployeeAndContractYnAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, boolean contractYn, Sort sort, LocalDate start, LocalDate finish, List<CustomerType> customerTypes);
    List<Customer> findByEmployeeAndContractYnAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(Employee employee, boolean contractYn, Sort sort, LocalDate start, LocalDate finish, CustomerType customerType);
    List<Customer> findByEmployeeAndContractYnAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, boolean contractYn, Sort sort, LocalDateTime start, LocalDateTime finish, List<CustomerType> customerTypes);
    List<Customer> findByEmployeeAndContractYnAndCreatedAtBetweenAndCustomerTypeAndDelYnFalse(Employee employee, boolean contractYn, Sort sort, LocalDateTime start, LocalDateTime finish, CustomerType customerType);

    // 상담현황별
    List<Customer> findByEmployeeAndConsultationStatusAndDelYnFalse(Employee employee, ConsultationStatus consultationStatus, Sort sort);
    List<Customer> findByEmployeeAndConsultationStatusAndCustomerTypeAndDelYnFalse(Employee employee, ConsultationStatus consultationStatus, CustomerType customerType, Sort sort);
    List<Customer> findByEmployeeAndConsultationStatusAndRegisterDateBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, ConsultationStatus consultationStatus, Sort sort, LocalDate start, LocalDate finish, List<CustomerType> customerTypes);
    List<Customer> findByEmployeeAndConsultationStatusAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(Employee employee, ConsultationStatus consultationStatus, Sort sort, LocalDate start, LocalDate finish, CustomerType customerType);
    List<Customer> findByEmployeeAndConsultationStatusAndCreatedAtBetweenAndCustomerTypeInAndDelYnFalse(Employee employee, ConsultationStatus consultationStatus, Sort sort, LocalDateTime start, LocalDateTime finish, List<CustomerType> customerTypes);
    List<Customer> findByEmployeeAndConsultationStatusAndCreatedAtBetweenAndCustomerTypeAndDelYnFalse(Employee employee, ConsultationStatus consultationStatus, Sort sort, LocalDateTime start, LocalDateTime finish, CustomerType customerType);


    // 이름 검색
    List<Customer> findByEmployeeAndNameContainsAndDelYnFalse(Employee employee, String name);

    // -----------------------------------------------------------------------------
    // 성과분석(analysis) RegisterDate
    // 이번달 DB 유형 고객들
    long countByEmployeeAndRegisterDateBetweenAndDelYnFalseAndCustomerType(Employee employee, LocalDate start, LocalDate finish, CustomerType customerType);

    // 이번달 ETC 유형 고객들
    long countByEmployeeAndCreatedAtBetweenAndDelYnFalseAndCustomerType(Employee employee, LocalDateTime start, LocalDateTime finish, CustomerType customerType);

    // customer의 상담현황 확률
    List<Customer> findByEmployeeAndConsultationStatusModifiedAtBetweenAndCustomerTypeAndDelYnFalse(Employee employee, LocalDateTime start, LocalDateTime finish, CustomerType customerType);


    // 계산
//    List<Customer> findByEmployeeAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(
//            Employee employee,
//            LocalDate createdAtStart,
//            LocalDate createdAtFinish,
//            CustomerType customerType
//    );
//    // all 계산
//    List<Customer> findAllByEmployeeAndRegisterDateBetweenAndCustomerTypeAndDelYnFalse(
//            Employee employee,
//            LocalDate start,
//            LocalDate finish,
//            List<CustomerType> customerTypes
//    );


    // 성과분석 확인(다시 계산할지 여부 확인)
    @Query("SELECT c FROM Customer c " +
            "WHERE (c.employee = :employee AND (c.createdAt >= :createdAtAfter OR c.modifiedAt >= :modifiedAfter))")
    List<Customer> findByEmployeeAndCreatedAtGreaterThanEqualOrModifiedAtGreaterThanEqual(
            Employee employee,
            LocalDateTime createdAtAfter,
            LocalDateTime modifiedAfter
    );
}
