package ga.backend.dong.controller;

import ga.backend.dong.dto.DongRequestDto;
import ga.backend.dong.dto.DongResponseDto;
import ga.backend.dong.entity.Dong;
import ga.backend.dong.mapper.DongMapper;
import ga.backend.dong.service.DongService;
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
public class DongController {
    private final DongService dongService;
    private final DongMapper dongMapper;

    // CREATE
    @PostMapping("/dong")
    public ResponseEntity postDong(@Valid @RequestBody DongRequestDto.Post post) {
        Dong dong = dongService.createDong(dongMapper.dongPostDtoToDong(post));
        DongResponseDto.Response response = dongMapper.dongToDongResponseDto(dong);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/dong/{dong-pk}")
    public ResponseEntity getDong(@Positive @PathVariable("dong-pk") long dongPk) {
        Dong dong = dongService.findDong(dongPk);
        DongResponseDto.Response response = dongMapper.dongToDongResponseDto(dong);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 구에 해당하는 동 내용 반환
    @GetMapping("/dongs/all")
    public ResponseEntity getDongs() {
        List<Dong> dongs = dongService.findDongs();
        List<DongResponseDto.Response> responses = dongMapper.dongToListDongResponseDto(dongs);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/dongs")
    public ResponseEntity getGus(@Valid @RequestParam("gu") long guPk) {
        List<Dong> dongs = dongService.findDongs(guPk);
        List<DongResponseDto.Response> responses = dongMapper.dongToListDongResponseDto(dongs);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/dong/{dong-pk}")
    public ResponseEntity patchDong(@Positive @PathVariable("dong-pk") long dongPk,
                                    @Valid @RequestBody DongRequestDto.Patch patch) {
        patch.setPk(dongPk);
        Dong dong = dongService.patchDong(dongMapper.dongPatchDtoToDong(patch));
        DongResponseDto.Response response = dongMapper.dongToDongResponseDto(dong);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/dong/{dong-pk}")
    public ResponseEntity deleteDong(@Positive @PathVariable("dong-pk") long dongPk) {
        dongService.deleteDong(dongPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
