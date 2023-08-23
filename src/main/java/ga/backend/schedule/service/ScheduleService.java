package ga.backend.schedule.service;


import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.schedule.entity.Schedule;
import ga.backend.schedule.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRespository;

    // CREATE
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRespository.save(schedule);
    }

    // READ
    public Schedule findSchedule(long schedulePk) {
        Schedule schedule = verifiedSchedule(schedulePk);
        return schedule;
    }

    // UPDATE
    public Schedule patchSchedule(Schedule schedule) {
        Schedule findSchedule = verifiedSchedule(schedule.getPk());
        Optional.ofNullable(schedule.getMemo()).ifPresent(findSchedule::setMemo);
        Optional.ofNullable(schedule.getDate()).ifPresent(findSchedule::setDate);
        Optional.ofNullable(schedule.getStartTm()).ifPresent(findSchedule::setStartTm);
        Optional.ofNullable(schedule.getFinishTm()).ifPresent(findSchedule::setFinishTm);
        Optional.ofNullable(schedule.getTime()).ifPresent(findSchedule::setTime);
        Optional.ofNullable(schedule.getAddress()).ifPresent(findSchedule::setAddress);

        return scheduleRespository.save(findSchedule);
    }

    // DELETE
    public void deleteSchedule(long schedulePk) {
        Schedule schedule = verifiedSchedule(schedulePk);
        scheduleRespository.delete(schedule);
    }

    // 검증
    public Schedule verifiedSchedule(long schedulePk) {
        Optional<Schedule> schedule = scheduleRespository.findById(schedulePk);
        return schedule.orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));
    }
}
