package ga.backend.scheduleprogress.controller;

import ga.backend.scheduleprogress.dto.ScheduleProgressRequestDto;
import ga.backend.scheduleprogress.dto.ScheduleProgressResponseDto;
import ga.backend.scheduleprogress.entity.ScheduleProgress;
import ga.backend.scheduleprogress.mapper.ScheduleProgressMapper;
import ga.backend.scheduleprogress.service.ScheduleProgressService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl + "/scheduleProgress")
@Validated
@AllArgsConstructor
public class ScheduleProgressController {
    private final ScheduleProgressService scheduleProgressService;
    private final ScheduleProgressMapper scheduleProgressMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postScheduleProgress(@Valid @RequestBody ScheduleProgressRequestDto.Post post) {
        ScheduleProgress scheduleProgress = scheduleProgressService.createScheduleProgress(scheduleProgressMapper.scheduleProgressPostDtoToScheduleProgress(post));
        ScheduleProgressResponseDto.Response response = scheduleProgressMapper.scheduleProgressToScheduleProgressResponseDto(scheduleProgress);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{scheduleProgress-pk}")
    public ResponseEntity getScheduleProgress(@Positive @PathVariable("scheduleProgress-pk") long scheduleProgressPk) {
        ScheduleProgress scheduleProgress = scheduleProgressService.findScheduleProgress(scheduleProgressPk);
        ScheduleProgressResponseDto.Response response = scheduleProgressMapper.scheduleProgressToScheduleProgressResponseDto(scheduleProgress);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchScheduleProgress(@Valid @RequestBody ScheduleProgressRequestDto.Patch patch) {
        ScheduleProgress scheduleProgress = scheduleProgressService.patchScheduleProgress(scheduleProgressMapper.scheduleProgressPatchDtoToScheduleProgress(patch));
        ScheduleProgressResponseDto.Response response = scheduleProgressMapper.scheduleProgressToScheduleProgressResponseDto(scheduleProgress);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{scheduleProgress-pk}")
    public ResponseEntity deleteScheduleProgress(@Positive @PathVariable("scheduleProgress-pk") long scheduleProgressPk) {
        scheduleProgressService.deleteScheduleProgress(scheduleProgressPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
