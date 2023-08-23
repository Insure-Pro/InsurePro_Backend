package ga.backend.scheduleprogress.repository;

import ga.backend.scheduleprogress.entity.ScheduleProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleProgressRepository extends JpaRepository<ScheduleProgress, Long> {
}
