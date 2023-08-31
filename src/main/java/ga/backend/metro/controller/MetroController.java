package ga.backend.metro.controller;

import ga.backend.metro.dto.MetroRequestDto;
import ga.backend.metro.dto.MetroResponseDto;
import ga.backend.metro.entity.Metro;
import ga.backend.metro.mapper.MetroMapper;
import ga.backend.metro.service.MetroService;
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
public class MetroController {
    private final MetroService metroService;
    private final MetroMapper metroMapper;

    // CREATE
    @PostMapping("/metro")
    public ResponseEntity postMetro(@Valid @RequestBody MetroRequestDto.Post post) {
        Metro metro = metroService.createMetro(metroMapper.metroPostDtoToMetro(post));
        MetroResponseDto.Response response = metroMapper.metroToMetroResponseDto(metro);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/metro/{metro-pk}")
    public ResponseEntity getMetro(@Positive @PathVariable("metro-pk") long metroPk) {
        Metro metro = metroService.findMetro(metroPk);
        MetroResponseDto.Response response = metroMapper.metroToMetroResponseDto(metro);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // 모든 내용 반환
    @GetMapping("/metros")
    public ResponseEntity getMetros() {
        List<Metro> metros = metroService.findMetros();
        List<MetroResponseDto.Response> responses = metroMapper.metroToListMetroResponseDto(metros);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/metro")
    public ResponseEntity patchMetro(@Valid @RequestBody MetroRequestDto.Patch patch) {
        Metro metro = metroService.patchMetro(metroMapper.metroPatchDtoToMetro(patch));
        MetroResponseDto.Response response = metroMapper.metroToMetroResponseDto(metro);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/metro/{metro-pk}")
    public ResponseEntity deleteMetro(@Positive @PathVariable("metro-pk") long metroPk) {
        metroService.deleteMetro(metroPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
