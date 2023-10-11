package ga.backend.schedule.repository;

import ga.backend.customer.entity.Customer;
import ga.backend.employee.entity.Employee;
import ga.backend.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCustomer(Customer customer);

    // all_history_count 계산
    List<Schedule> findByEmployeeAndCreatedAtBetween(
            Employee employee,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtFinish
    );

    // 이번달에 등록된 히스토리 유형 'Schedule.Progress' 의 개수
    List<Schedule> findByEmployeeAndCreatedAtBetweenAndProgress(
            Employee employee,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtFinish,
            Schedule.Progress progress
    );

    // 성과분석 확인(다시 계산)
    @Query("SELECT s FROM Schedule s " +
            "WHERE (s.employee = :employee AND (s.createdAt >= :createdAtAfter OR s.modifiedAt >= :modifiedAfter))")
    List<Schedule> findByEmployeeAndCreatedAtGreaterThanEqualOrModifiedAtGreaterThanEqual(
            Employee employee,
            LocalDateTime createdAtAfter,
            LocalDateTime modifiedAfter
    );
}