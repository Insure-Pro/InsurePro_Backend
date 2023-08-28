package ga.backend.progress.controller;

import ga.backend.progress.dto.ProgressRequestDto;
import ga.backend.progress.dto.ProgressResponseDto;
import ga.backend.progress.entity.Progress;
import ga.backend.progress.mapper.ProgressMapper;
import ga.backend.progress.service.ProgressService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl + "/progress")
@Validated
@AllArgsConstructor
public class ProgressController {
    private final ProgressService progressService;
    private final ProgressMapper progressMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postProgress(@Valid @RequestBody ProgressRequestDto.Post post) {
        Progress progress = progressService.createProgress(progressMapper.progressPostDtoToProgress(post));
        ProgressResponseDto.Response response = progressMapper.progressToProgressResponseDto(progress);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{progress-pk}")
    public ResponseEntity getProgress(@Positive @PathVariable("progress-pk") long progressPk) {
        Progress progress = progressService.findProgress(progressPk);
        ProgressResponseDto.Response response = progressMapper.progressToProgressResponseDto(progress);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchProgress(@Valid @RequestBody ProgressRequestDto.Patch patch) {
        Progress progress = progressService.patchProgress(progressMapper.progressPatchDtoToProgress(patch));
        ProgressResponseDto.Response response = progressMapper.progressToProgressResponseDto(progress);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{progress-pk}")
    public ResponseEntity deleteProgress(@Positive @PathVariable("progress-pk") long progressPk) {
        progressService.deleteProgress(progressPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
