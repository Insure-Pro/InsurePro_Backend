package ga.backend.dong2.controller;

import ga.backend.dong2.dto.DongRequestDto;
import ga.backend.dong2.dto.DongResponseDto;
import ga.backend.dong2.entity.Dong2;
import ga.backend.dong2.mapper.Dong2Mapper;
import ga.backend.dong2.service.Dong2Service;
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
public class Dong2Controller {
    private final Dong2Service dongService;
    private final Dong2Mapper dongMapper;

    // CREATE
    @PostMapping("/dong2")
    public ResponseEntity postDong(@Valid @RequestBody DongRequestDto.Post post) {
        Dong2 dong = dongService.createDong(dongMapper.dongPostDtoToDong(post));
        DongResponseDto.Response response = dongMapper.dongToDongResponseDto(dong);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/dong2/{dong-pk}")
    public ResponseEntity getDong(@Positive @PathVariable("dong-pk") long dongPk) {
        Dong2 dong = dongService.findDong(dongPk);
        DongResponseDto.Response response = dongMapper.dongToDongResponseDto(dong);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 구에 해당하는 동 내용 반환
    @GetMapping("/dong2s/all")
    public ResponseEntity getDongs() {
        List<Dong2> dongs = dongService.findDongs();
        List<DongResponseDto.Response> responses = dongMapper.dongToListDongResponseDto(dongs);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/dong2s")
    public ResponseEntity getGus(@Valid @RequestParam("gu") long guPk) {
        List<Dong2> dongs = dongService.findDongs(guPk);
        List<DongResponseDto.Response> responses = dongMapper.dongToListDongResponseDto(dongs);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/dong2/{dong-pk}")
    public ResponseEntity patchDong(@Positive @PathVariable("dong-pk") long dongPk,
                                    @Valid @RequestBody DongRequestDto.Patch patch) {
        patch.setPk(dongPk);
        Dong2 dong = dongService.patchDong(dongMapper.dongPatchDtoToDong(patch));
        DongResponseDto.Response response = dongMapper.dongToDongResponseDto(dong);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/dong2/{dong-pk}")
    public ResponseEntity deleteDong(@Positive @PathVariable("dong-pk") long dongPk) {
        dongService.deleteDong(dongPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
