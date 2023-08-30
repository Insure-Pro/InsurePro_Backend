package ga.backend.gu.controller;

import ga.backend.gu.dto.GuRequestDto;
import ga.backend.gu.dto.GuResponseDto;
import ga.backend.gu.entity.Gu;
import ga.backend.gu.mapper.GuMapper;
import ga.backend.gu.service.GuService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl + "/gu")
@Validated
@AllArgsConstructor
public class GuController {
    private final GuService guService;
    private final GuMapper guMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postGu(@Valid @RequestBody GuRequestDto.Post post) {
        Gu gu = guService.createGu(guMapper.guPostDtoToGu(post));
        GuResponseDto.Response response = guMapper.guToGuResponseDto(gu);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{gu-pk}")
    public ResponseEntity getGu(@Positive @PathVariable("gu-pk") long guPk) {
        Gu gu = guService.findGu(guPk);
        GuResponseDto.Response response = guMapper.guToGuResponseDto(gu);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchGu(@Valid @RequestBody GuRequestDto.Patch patch) {
        Gu gu = guService.patchGu(guMapper.guPatchDtoToGu(patch));
        GuResponseDto.Response response = guMapper.guToGuResponseDto(gu);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{gu-pk}")
    public ResponseEntity deleteGu(@Positive @PathVariable("gu-pk") long guPk) {
        guService.deleteGu(guPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
