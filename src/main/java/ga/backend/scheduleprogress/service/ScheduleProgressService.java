package ga.backend.scheduleprogress.service;

import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.scheduleprogress.entity.ScheduleProgress;
import ga.backend.scheduleprogress.repository.ScheduleProgressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleProgressService {
    private final ScheduleProgressRepository scheduleProgressRespository;

    // CREATE
    public ScheduleProgress createScheduleProgress(ScheduleProgress scheduleProgress) {
        return scheduleProgressRespository.save(scheduleProgress);
    }

    // READ
    public ScheduleProgress findScheduleProgress(long scheduleProgressPk) {
        ScheduleProgress scheduleProgress = verifiedScheduleProgress(scheduleProgressPk);
        return scheduleProgress;
    }

    // UPDATE
    public ScheduleProgress patchScheduleProgress(ScheduleProgress scheduleProgress) {
        ScheduleProgress findScheduleProgress = verifiedScheduleProgress(scheduleProgress.getPk());
        return scheduleProgressRespository.save(findScheduleProgress);
    }

    // DELETE
    public void deleteScheduleProgress(long scheduleProgressPk) {
        ScheduleProgress scheduleProgress = verifiedScheduleProgress(scheduleProgressPk);
        scheduleProgressRespository.delete(scheduleProgress);
    }

    // 검증
    public ScheduleProgress verifiedScheduleProgress(long scheduleProgressPk) {
        Optional<ScheduleProgress> scheduleProgress = scheduleProgressRespository.findById(scheduleProgressPk);
        return scheduleProgress.orElseThrow(() -> new BusinessLogicException(ExceptionCode.DONG_NOT_FOUND));
    }
}
