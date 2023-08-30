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

@RestController
@RequestMapping(Version.currentUrl + "/schedule")
@Validated
@AllArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postSchedule(@Valid @RequestBody ScheduleRequestDto.Post post) {
        Schedule schedule = scheduleService.createSchedule(scheduleMapper.schedulePostDtoToSchedule(post));
        ScheduleResponseDto.Response response = scheduleMapper.scheduleToScheduleResponseDto(schedule);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{schedule-pk}")
    public ResponseEntity getSchedule(@Positive @PathVariable("schedule-pk") long schedulePk) {
        Schedule schedule = scheduleService.findSchedule(schedulePk);
        ScheduleResponseDto.Response response = scheduleMapper.scheduleToScheduleResponseDto(schedule);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchSchedule(@Valid @RequestBody ScheduleRequestDto.Patch patch) {
        Schedule schedule = scheduleService.patchSchedule(scheduleMapper.schedulePatchDtoToSchedule(patch));
        ScheduleResponseDto.Response response = scheduleMapper.scheduleToScheduleResponseDto(schedule);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{schedule-pk}")
    public ResponseEntity deleteSchedule(@Positive @PathVariable("schedule-pk") long schedulePk) {
        scheduleService.deleteSchedule(schedulePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
