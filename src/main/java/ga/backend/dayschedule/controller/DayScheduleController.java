package ga.backend.dayschedule.controller;

import ga.backend.dayschedule.dto.DayScheduleRequestDto;
import ga.backend.dayschedule.dto.DayScheduleResponseDto;
import ga.backend.dayschedule.entity.DaySchedule;
import ga.backend.dayschedule.mapper.DayScheduleMapper;
import ga.backend.dayschedule.service.DayScheduleService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl + "/daySchedule")
@Validated
@AllArgsConstructor
public class DayScheduleController {
    private final DayScheduleService dayScheduleService;
    private final DayScheduleMapper dayScheduleMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postDaySchedule(@Valid @RequestBody DayScheduleRequestDto.Post post) {
        DaySchedule daySchedule = dayScheduleService.createDaySchedule(dayScheduleMapper.daySchedulePostDtoToDaySchedule(post));
        DayScheduleResponseDto.Response response = dayScheduleMapper.dayScheduleToDayScheduleResponseDto(daySchedule);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{daySchedule-pk}")
    public ResponseEntity getDaySchedule(@Positive @PathVariable("daySchedule-pk") long daySchedulePk) {
        DaySchedule daySchedule = dayScheduleService.findDaySchedule(daySchedulePk);
        DayScheduleResponseDto.Response response = dayScheduleMapper.dayScheduleToDayScheduleResponseDto(daySchedule);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchDaySchedule(@Valid @RequestBody DayScheduleRequestDto.Patch patch) {
        DaySchedule daySchedule = dayScheduleService.patchDaySchedule(dayScheduleMapper.daySchedulePatchDtoToDaySchedule(patch));
        DayScheduleResponseDto.Response response = dayScheduleMapper.dayScheduleToDayScheduleResponseDto(daySchedule);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // DELETE
    @DeleteMapping("/{daySchedule-pk}")
    public ResponseEntity deleteDaySchedule(@Positive @PathVariable("daySchedule-pk") long daySchedulePk) {
        dayScheduleService.deleteDaySchedule(daySchedulePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
