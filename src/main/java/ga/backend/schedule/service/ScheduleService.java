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
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRespository;
    private final CustomerService customerService;
    private final FindEmployee findEmployee;

    // CREATE
    public Schedule createSchedule(Schedule schedule, long customerPk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        Customer customer = customerService.findCustomer(customerPk);

        schedule.setEmployee(employee);
        schedule.setCustomer(customer);

        // 만나는 시간(time) 자동 계산
        LocalTime time = findScheduleTime(schedule.getStartTm(), schedule.getFinishTm());
        Optional.ofNullable(time).ifPresent(schedule::setTime);

        return scheduleRespository.save(schedule);
    }

    // READ
    public Schedule findSchedule(long schedulePk) {
        Schedule schedule = verifiedSchedule(schedulePk);
        return schedule;
    }

    public List<Schedule> findSchedulesByCustomer(long customerPk) {
        Customer customer = customerService.findCustomer(customerPk);
        return scheduleRespository.findByCustomerAndDelYnFalse(customer);
    }

    // UPDATE
    public Schedule patchSchedule(Schedule schedule) {
        LocalTime time = findScheduleTime(schedule.getStartTm(), schedule.getStartTm());

        Schedule findSchedule = verifiedSchedule(schedule.getPk());
        Optional.ofNullable(schedule.getMemo()).ifPresent(findSchedule::setMemo);
        Optional.ofNullable(schedule.getDate()).ifPresent(findSchedule::setDate);
        Optional.ofNullable(schedule.getStartTm()).ifPresent(findSchedule::setStartTm);
        Optional.ofNullable(schedule.getFinishTm()).ifPresent(findSchedule::setFinishTm);
        Optional.ofNullable(schedule.getAddress()).ifPresent(findSchedule::setAddress);
        Optional.ofNullable(schedule.getMeetYn()).ifPresent(findSchedule::setMeetYn);
        Optional.ofNullable(schedule.getDelYn()).ifPresent(findSchedule::setDelYn);
        Optional.ofNullable(schedule.getColor()).ifPresent(findSchedule::setColor);
        Optional.ofNullable(schedule.getProgress()).ifPresent(findSchedule::setProgress);


        if(schedule.getStartTm() != null && schedule.getFinishTm() != null)
            findSchedule.setTime(findScheduleTime(schedule.getStartTm(), schedule.getFinishTm()));
        else if(schedule.getStartTm() != null && findSchedule.getFinishTm() != null)
            findSchedule.setTime(findScheduleTime(schedule.getStartTm(), findSchedule.getFinishTm()));
        else if(findSchedule.getStartTm() != null && schedule.getFinishTm() != null)
            findSchedule.setTime(findScheduleTime(findSchedule.getStartTm(), schedule.getFinishTm()));

        return scheduleRespository.save(findSchedule);
    }

    // time 컬럼값 구하기
    public LocalTime findScheduleTime(LocalTime startTm, LocalTime finishTm) {
        System.out.println("!! startTm : " + startTm);
        System.out.println("!! finishTm : " + finishTm);
        if(startTm!= null && finishTm != null) {
            int hour = finishTm.getHour() - startTm.getHour();
            int minute = finishTm.getMinute() - startTm.getMinute();
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
        Optional<Schedule> schedule = scheduleRespository.findByPkAndDelYnFalse(schedulePk);
        return schedule.orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));
    }
}
