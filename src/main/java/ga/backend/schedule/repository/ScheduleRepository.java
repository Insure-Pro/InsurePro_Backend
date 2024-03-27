package ga.backend.schedule.repository;

import ga.backend.customer.entity.Customer;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.employee.entity.Employee;
import ga.backend.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCustomerAndDelYnFalse(Customer customer);
    Optional<Schedule> findByPkAndDelYnFalse(long schedulePk);

    // ------------------------------------------------------------------------------------
    // analysis

    // all_history_count 계산
    List<Schedule> findByEmployeeAndCreatedAtBetweenAndCustomerCustomerTypeAndDelYnFalse(
            Employee employee,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtFinish,
            CustomerType customerType
    );

    // 이번달에 등록된 히스토리 유형 'Schedule.Progress' 의 개수
//    List<Schedule> findByEmployeeAndCreatedAtBetweenAndProgressAndDelYnFalse(
//            Employee employee,
//            LocalDateTime createdAtStart,
//            LocalDateTime createdAtFinish,
//            Schedule.Progress progress
//    );

    // 성과분석 확인(다시 계산할지 여부 확인)
    @Query("SELECT s FROM Schedule s " +
            "WHERE (s.employee = :employee AND (s.createdAt >= :createdAtAfter OR s.modifiedAt >= :modifiedAfter))")
    List<Schedule> findByEmployeeAndCreatedAtGreaterThanEqualOrModifiedAtGreaterThanEqual(
            Employee employee,
            LocalDateTime createdAtAfter,
            LocalDateTime modifiedAfter
    );
}