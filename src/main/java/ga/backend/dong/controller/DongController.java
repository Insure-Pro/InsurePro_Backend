package ga.backend.dong.controller;

import ga.backend.dong.dto.DongRequestDto;
import ga.backend.dong.dto.DongResponseDto;
import ga.backend.dong.entity.Dong;
import ga.backend.dong.mapper.DongMapper;
import ga.backend.dong.service.DongService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/dong")
@Validated
@AllArgsConstructor
public class DongController {
    private final DongService dongService;
    private final DongMapper dongMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postDong(@Valid @RequestBody DongRequestDto.Post post) {
        Dong dong = dongService.createDong(dongMapper.dongPostDtoToDong(post));
        DongResponseDto.Response response = dongMapper.dongToDongResponseDto(dong);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/{dong-pk}")
    public ResponseEntity getDong(@Positive @PathVariable("dong-pk") long dongPk) {
        Dong dong = dongService.findDong(dongPk);
        DongResponseDto.Response response = dongMapper.dongToDongResponseDto(dong);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // UPDATE
    @PatchMapping
    public ResponseEntity patchDong(@Valid @RequestBody DongRequestDto.Patch patch) {
        Dong dong = dongService.patchDong(dongMapper.dongPatchDtoToDong(patch));
        DongResponseDto.Response response = dongMapper.dongToDongResponseDto(dong);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // DELETE
    @DeleteMapping("/{dong-pk}")
    public ResponseEntity deleteDong(@Positive @PathVariable("dong-pk") long dongPk) {
        dongService.deleteDong(dongPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
