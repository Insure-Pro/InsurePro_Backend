package ga.backend.dayschedule.service;

import ga.backend.dayschedule.entity.DaySchedule;
import ga.backend.dayschedule.repository.DayScheduleRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DayScheduleService {
    private final DayScheduleRepository dayScheduleRespository;

    // CREATE
    public DaySchedule createDaySchedule(DaySchedule daySchedule) {
        return dayScheduleRespository.save(daySchedule);
    }

    // READ
    public DaySchedule findDaySchedule(long daySchedulePk) {
        DaySchedule daySchedule = verifiedDaySchedule(daySchedulePk);
        return daySchedule;
    }

    // UPDATE
    public DaySchedule patchDaySchedule(DaySchedule daySchedule) {
        DaySchedule findDaySchedule = verifiedDaySchedule(daySchedule.getPk());
        Optional.ofNullable(daySchedule.getContent()).ifPresent(findDaySchedule::setContent);
        Optional.ofNullable(daySchedule.getEmployee()).ifPresent(findDaySchedule::setEmployee);
        return dayScheduleRespository.save(findDaySchedule);
    }

    // DELETE
    public void deleteDaySchedule(long daySchedulePk) {
        DaySchedule daySchedule = verifiedDaySchedule(daySchedulePk);
        dayScheduleRespository.delete(daySchedule);
    }

    // 검증
    public DaySchedule verifiedDaySchedule(long daySchedulePk) {
        Optional<DaySchedule> daySchedule = dayScheduleRespository.findById(daySchedulePk);
        return daySchedule.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMPANY_NOT_FOUND));
    }
}
