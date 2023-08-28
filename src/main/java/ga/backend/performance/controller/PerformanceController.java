package ga.backend.performance.controller;

import ga.backend.performance.dto.PerformanceRequestDto;
import ga.backend.performance.dto.PerformanceResponseDto;
import ga.backend.performance.entity.Performance;
import ga.backend.performance.mapper.PerformanceMapper;
import ga.backend.performance.service.PerformanceService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl + "/performance")
@Validated
@AllArgsConstructor
public class PerformanceController {
    private final PerformanceService performanceService;
    private final PerformanceMapper performanceMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postPerformance(@Valid @RequestBody PerformanceRequestDto.Post post) {
        Performance performance = performanceService.createPerformance(performanceMapper.performancePostDtoToPerformance(post));
        PerformanceResponseDto.Response response = performanceMapper.performanceToPerformanceResponseDto(performance);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{performance-pk}")
    public ResponseEntity getPerformance(@Positive @PathVariable("performance-pk") long performancePk) {
        Performance performance = performanceService.findPerformance(performancePk);
        PerformanceResponseDto.Response response = performanceMapper.performanceToPerformanceResponseDto(performance);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchPerformance(@Valid @RequestBody PerformanceRequestDto.Patch patch) {
        Performance performance = performanceService.patchPerformance(performanceMapper.performancePatchDtoToPerformance(patch));
        PerformanceResponseDto.Response response = performanceMapper.performanceToPerformanceResponseDto(performance);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{performance-pk}")
    public ResponseEntity deletePerformance(@Positive @PathVariable("performance-pk") long performancePk) {
        performanceService.deletePerformance(performancePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
