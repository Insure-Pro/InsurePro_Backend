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
import java.util.List;

@RestController
@RequestMapping(Version.currentUrl)
@Validated
@AllArgsConstructor
public class GuController {
    private final GuService guService;
    private final GuMapper guMapper;

    // CREATE
    @PostMapping("/gu")
    public ResponseEntity postGu(@Valid @RequestBody GuRequestDto.Post post) {
        Gu gu = guService.createGu(guMapper.guPostDtoToGu(post));
        GuResponseDto.Response response = guMapper.guToGuResponseDto(gu);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/gu/{gu-pk}")
    public ResponseEntity getGu(@Positive @PathVariable("gu-pk") long guPk) {
        Gu gu = guService.findGu(guPk);
        GuResponseDto.Response response = guMapper.guToGuResponseDto(gu);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 모든 내용 반환
    @GetMapping("/gus/all")
    public ResponseEntity getGus() {
        List<Gu> gus = guService.findGus();
        List<GuResponseDto.Response> responses = guMapper.guToListGuResponseDto(gus);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 광역시에 해당하는 구 내용 반환
    @GetMapping("/gus")
    public ResponseEntity getGus(@Valid @RequestParam("metro") long metroPk) {
        List<Gu> gus = guService.findGusByMetroPk(metroPk);
        List<GuResponseDto.Response> responses = guMapper.guToListGuResponseDto(gus);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/gu")
    public ResponseEntity patchGu(@Valid @RequestBody GuRequestDto.Patch patch) {
        Gu gu = guService.patchGu(guMapper.guPatchDtoToGu(patch));
        GuResponseDto.Response response = guMapper.guToGuResponseDto(gu);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/gu/{gu-pk}")
    public ResponseEntity deleteGu(@Positive @PathVariable("gu-pk") long guPk) {
        guService.deleteGu(guPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
