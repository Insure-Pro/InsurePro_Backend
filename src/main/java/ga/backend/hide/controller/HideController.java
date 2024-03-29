package ga.backend.hide.controller;

import ga.backend.employee.mapper.EmployeeMapper;
import ga.backend.hide.dto.HideRequestDto;
import ga.backend.hide.dto.HideResponseDto;
import ga.backend.hide.entity.Hide;
import ga.backend.hide.mapper.HideMapper;
import ga.backend.hide.service.HideService;
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
public class HideController {
    private final HideService hideService;
    private final HideMapper hideMapper;

    // CREATE
    @PostMapping("/hide")
    public ResponseEntity postTeam(@Positive @RequestParam(value = "customerTypePk") Long customerTypePk) {
        Hide hide = hideService.createHide(customerTypePk);
        HideResponseDto.Response response = hideMapper.HideToHideResponseDto(hide);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // DELETE
    @DeleteMapping("/hide")
    public ResponseEntity deleteTeam(@Positive @RequestParam(value = "customerTypePk") Long customerTypePk) {
        hideService.deleteHide(customerTypePk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
