package ga.backend.metro2.controller;

import ga.backend.metro2.dto.MetroRequestDto;
import ga.backend.metro2.dto.MetroResponseDto;
import ga.backend.metro2.mapper.Metro2Mapper;
import ga.backend.metro2.entity.Metro2;
import ga.backend.metro2.service.Metro2Service;
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
public class Metro2Controller {
    private final Metro2Service metroService;
    private final Metro2Mapper metroMapper;

    // CREATE
    @PostMapping("/metro2")
    public ResponseEntity postMetro(@Valid @RequestBody MetroRequestDto.Post post) {
        Metro2 metro = metroService.createMetro(metroMapper.metroPostDtoToMetro(post));
        MetroResponseDto.Response response = metroMapper.metroToMetroResponseDto(metro);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/metro2/{metro-pk}")
    public ResponseEntity getMetro(@Positive @PathVariable("metro-pk") long metroPk) {
        Metro2 metro = metroService.findMetro(metroPk);
        MetroResponseDto.Response response = metroMapper.metroToMetroResponseDto(metro);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // 모든 내용 반환
    @GetMapping("/metros2")
    public ResponseEntity getMetros() {
        List<Metro2> metros = metroService.findMetros();
        List<MetroResponseDto.Response> responses = metroMapper.metroToListMetroResponseDto(metros);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/metro2/{metro-pk}")
    public ResponseEntity patchMetro(@Positive @PathVariable("metro-pk") long metroPk,
                                     @Valid @RequestBody MetroRequestDto.Patch patch) {
        patch.setPk(metroPk);
        Metro2 metro = metroService.patchMetro(metroMapper.metroPatchDtoToMetro(patch));
        MetroResponseDto.Response response = metroMapper.metroToMetroResponseDto(metro);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/metro2/{metro-pk}")
    public ResponseEntity deleteMetro(@Positive @PathVariable("metro-pk") long metroPk) {
        metroService.deleteMetro(metroPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
