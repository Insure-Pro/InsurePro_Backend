package ga.backend.schedule.service;


import ga.backend.customer.entity.Customer;
import ga.backend.customer.service.CustomerService;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.schedule.entity.Schedule;
import ga.backend.schedule.repository.ScheduleRepository;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRespository;
    private final CustomerService customerService;
    private final FindEmployee findEmployee;

    // CREATE
    public Schedule createSchedule(Schedule schedule, long customer_id) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        Customer customer = customerService.findCustomer(customer_id);

        schedule.setEmployee(employee);
        schedule.setCustomer(customer);

        // 만나는 시간(time) 자동 계싼
        LocalTime time = findScheduleTime(schedule.getStartTm(), schedule.getStartTm());
        Optional.ofNullable(time).ifPresent(schedule::setTime);

        return scheduleRespository.save(schedule);
    }

    // READ
    public Schedule findSchedule(long schedulePk) {
        Schedule schedule = verifiedSchedule(schedulePk);
        return schedule;
    }

    // UPDATE
    public Schedule patchSchedule(Schedule schedule) {
        LocalTime time = findScheduleTime(schedule.getStartTm(), schedule.getStartTm());

        Schedule findSchedule = verifiedSchedule(schedule.getPk());
        Optional.ofNullable(schedule.getMemo()).ifPresent(findSchedule::setMemo);
        Optional.ofNullable(schedule.getDate()).ifPresent(findSchedule::setDate);
        Optional.ofNullable(schedule.getStartTm()).ifPresent(findSchedule::setStartTm);
        Optional.ofNullable(schedule.getFinishTm()).ifPresent(findSchedule::setFinishTm);
        Optional.ofNullable(time).ifPresent(findSchedule::setTime);
        Optional.ofNullable(schedule.getAddress()).ifPresent(findSchedule::setAddress);
        Optional.ofNullable(schedule.getMeetYn()).ifPresent(findSchedule::setMeetYn);
        Optional.ofNullable(schedule.getDelYn()).ifPresent(findSchedule::setDelYn);
        Optional.ofNullable(schedule.getColor()).ifPresent(findSchedule::setColor);
        Optional.ofNullable(schedule.getProgress()).ifPresent(findSchedule::setProgress);

        return scheduleRespository.save(findSchedule);
    }

    // time 컬럼값 구하기
    public LocalTime findScheduleTime(LocalTime startTm, LocalTime finishTm) {
        if(startTm!= null && finishTm != null) {
            int hour = startTm.getHour() - finishTm.getHour();
            int minute = startTm.getMinute() - finishTm.getMinute();
            return LocalTime.of(hour, minute);
        }

        return null;
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
