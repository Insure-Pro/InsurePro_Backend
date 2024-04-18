package ga.backend.ta.controller;

import ga.backend.employee.mapper.EmployeeMapper;
import ga.backend.ta.dto.TARequestDto;
import ga.backend.ta.dto.TAResponseDto;
import ga.backend.ta.entity.TA;
import ga.backend.ta.mapper.TAMapper;
import ga.backend.ta.service.TAService;
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
public class TAController {
    private final TAService teamService;
    private final TAMapper teamMapper;
    private final EmployeeMapper employeeMapper;

    // CREATE
    @PostMapping("/ta")
    public ResponseEntity postTA(@Valid @RequestBody TARequestDto.Post post) {
        TA ta = teamService.createTA(teamMapper.taPostDtoToTA(post), post.getCustomerPk());
        TAResponseDto.Response response = teamMapper.taToTAResponseDto(ta);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/ta/{ta-pk}")
    public ResponseEntity getTA(@Positive @PathVariable("ta-pk") long taPk) {
        TA ta = teamService.findTA(taPk);
        TAResponseDto.Response response = teamMapper.taToTAResponseDto(ta);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 고객별 TA들 조회
    @GetMapping("/tas/{customer-pk}")
    public ResponseEntity getTAByCustomerPk(@Positive @PathVariable("customer-pk") long customerPk) {
        List<TA> tas = teamService.findTAbyCustomerPk(customerPk);
        List<TAResponseDto.Response> responses = teamMapper.tasToTAResponseDtos(tas);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/ta/{ta-pk}")
    public ResponseEntity patchTA(@Positive @PathVariable("ta-pk") long taPk,
                                  @Valid @RequestBody TARequestDto.Patch patch) {
        patch.setPk(taPk);
        TA team = teamService.patchTA(teamMapper.taPatchDtoToTA(patch));
        TAResponseDto.Response response = teamMapper.taToTAResponseDto(team);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/ta/{ta-pk}")
    public ResponseEntity deleteTA(@Positive @PathVariable("ta-pk") long teamPk) {
        teamService.deleteTA(teamPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
