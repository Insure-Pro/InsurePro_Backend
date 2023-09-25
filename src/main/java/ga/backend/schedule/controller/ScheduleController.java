package ga.backend.schedule.controller;

import ga.backend.schedule.dto.ScheduleRequestDto;
import ga.backend.schedule.dto.ScheduleResponseDto;
import ga.backend.schedule.entity.Schedule;
import ga.backend.schedule.mapper.ScheduleMapper;
import ga.backend.schedule.service.ScheduleService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(Version.currentUrl)
@Validated
@AllArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    // CREATE
    @PostMapping("/schedule/{customer-pk}")
    public ResponseEntity postSchedule(@Valid @RequestBody ScheduleRequestDto.Post post,
                                       @Positive @PathVariable("customer-pk") long customerPk) {
        Schedule schedule = scheduleService.createSchedule(scheduleMapper.schedulePostDtoToSchedule(post), customerPk);
        ScheduleResponseDto.Response response = scheduleMapper.scheduleToScheduleResponseDto(schedule);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/schedule/{schedule-pk}")
    public ResponseEntity getSchedule(@Positive @PathVariable("schedule-pk") long schedulePk) {
        Schedule schedule = scheduleService.findSchedule(schedulePk);
        ScheduleResponseDto.Response response = scheduleMapper.scheduleToScheduleResponseDto(schedule);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 고객별 캘린더 조회
    @GetMapping("/schedules/{customer-pk}")
    public ResponseEntity getSchedules(@Positive @PathVariable("customer-pk") long customerPk) {
        List<Schedule> schedules = scheduleService.findSchedulesByCustomer(customerPk);
        List<ScheduleResponseDto.Response> rsesponses = scheduleMapper.schedulesToSchedulesResponseDto(schedules);

        return new ResponseEntity<>(rsesponses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/schedule/{schedule-pk}")
    public ResponseEntity patchSchedule(@Positive @PathVariable("schedule-pk") long schedulePk,
                                        @Valid @RequestBody ScheduleRequestDto.Patch patch) {
        patch.setPk(schedulePk);
        Schedule schedule = scheduleService.patchSchedule(scheduleMapper.schedulePatchDtoToSchedule(patch));
        ScheduleResponseDto.Response response = scheduleMapper.scheduleToScheduleResponseDto(schedule);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/schedule/{schedule-pk}")
    public ResponseEntity deleteSchedule(@Positive @PathVariable("schedule-pk") long schedulePk) {
        scheduleService.deleteSchedule(schedulePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}