package ga.backend.li.controller;

import ga.backend.li.dto.LiRequestDto;
import ga.backend.li.dto.LiResponseDto;
import ga.backend.li.entity.Li;
import ga.backend.li.mapper.LiMapper;
import ga.backend.li.service.LiService;
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
public class LiController {
    private final LiService liService;
    private final LiMapper liMapper;

    // CREATE
    @PostMapping("/li")
    public ResponseEntity postLi(@Valid @RequestBody LiRequestDto.Post post) {
        Li li = liService.createLi(liMapper.liPostDtoToLi(post), post.getDongPk());
        LiResponseDto.Response response = liMapper.liToLiResponseDto(li);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/li/{li-pk}")
    public ResponseEntity getLi(@Positive @PathVariable("li-pk") long liPk) {
        Li li = liService.findLi(liPk);
        LiResponseDto.Response response = liMapper.liToLiResponseDto(li);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 구에 해당하는 동 내용 반환
    @GetMapping("/lis/all")
    public ResponseEntity getLis() {
        List<Li> lis = liService.findLis();
        List<LiResponseDto.Response> responses = liMapper.liToListLiResponseDto(lis);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/lis")
    public ResponseEntity getGus(@Valid @RequestParam("dong") long dongPk) {
        List<Li> lis = liService.findLis(dongPk);
        List<LiResponseDto.Response> responses = liMapper.liToListLiResponseDto(lis);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // UPDATE
    @PatchMapping("/li/{li-pk}")
    public ResponseEntity patchLi(@Positive @PathVariable("li-pk") long liPk,
                                  @Valid @RequestBody LiRequestDto.Patch patch) {
        patch.setPk(liPk);
        Li li = liService.patchLi(liMapper.liPatchDtoToLi(patch));
        LiResponseDto.Response response = liMapper.liToLiResponseDto(li);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/li/{li-pk}")
    public ResponseEntity deleteLi(@Positive @PathVariable("li-pk") long liPk) {
        liService.deleteLi(liPk);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
