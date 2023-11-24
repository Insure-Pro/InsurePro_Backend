package ga.backend.gu2.controller;

import ga.backend.gu2.entity.Gu2;
import ga.backend.gu2.dto.GuRequestDto;
import ga.backend.gu2.dto.GuResponseDto;
import ga.backend.gu2.mapper.Gu2Mapper;
import ga.backend.gu2.service.Gu2Service;
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
public class Gu2Controller {
    private final Gu2Service guService;
    private final Gu2Mapper guMapper;

    // CREATE
    @PostMapping("/gu2")
    public ResponseEntity postGu(@Valid @RequestBody GuRequestDto.Post post) {
        Gu2 gu = guService.createGu(guMapper.guPostDtoToGu(post));
        GuResponseDto.Response response = guMapper.guToGuResponseDto(gu);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/gu2/{gu-pk}")
    public ResponseEntity getGu(@Positive @PathVariable("gu-pk") long guPk) {
        Gu2 gu = guService.findGu(guPk);
        GuResponseDto.Response response = guMapper.guToGuResponseDto(gu);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 모든 내용 반환
    @GetMapping("/gus2/all")
    public ResponseEntity getGus() {
        List<Gu2> gus = guService.findGus();
        List<GuResponseDto.Response> responses = guMapper.guToListGuResponseDto(gus);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 광역시에 해당하는 구 내용 반환
    @GetMapping("/gus2")
    public ResponseEntity getGus(@Valid @RequestParam("metro") long metroPk) {
        List<Gu2> gus = guService.findGusByMetroPk(metroPk);
        List<GuResponseDto.Response> responses = guMapper.guToListGuResponseDto(gus);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/gu2/{gu-pk}")
    public ResponseEntity patchGu(@Positive @PathVariable("gu-pk") long guPk,
                                  @Valid @RequestBody GuRequestDto.Patch patch) {
        patch.setPk(guPk);
        Gu2 gu = guService.patchGu(guMapper.guPatchDtoToGu(patch));
        GuResponseDto.Response response = guMapper.guToGuResponseDto(gu);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/gu2/{gu-pk}")
    public ResponseEntity deleteGu(@Positive @PathVariable("gu-pk") long guPk) {
        guService.deleteGu(guPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
