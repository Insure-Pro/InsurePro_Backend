package ga.backend.photo.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import ga.backend.photo.dto.PhotoRequestDto;
import ga.backend.photo.dto.PhotoResponseDto;
import ga.backend.photo.entity.Photo;
import ga.backend.photo.mapper.PhotoMapper;
import ga.backend.photo.service.PhotoService;
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
@RequestMapping(Version.currentUrl + "/photos")
@Validated
@AllArgsConstructor
public class PhotoController {
    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    /**
     *
     * @param post
     * @return {
     * 	"photo_pk" : 1,
     * 	"name" : "패스파인더"
     * }
     */
    @PostMapping
    public ResponseEntity postPhoto(@Valid @RequestBody PhotoRequestDto.Post post) {

        // 생성
        Photo photo = photoService.createPhoto(photoMapper.photoPostDtoToPhoto(post));

        // 응답
        PhotoResponseDto.Response response = photoMapper.photoToPhotoResponseDto(photo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     *
     * @param pk
     * @param name
     * @return {
     *     "photos" : [
     *         {
     *             "photo_pk" : "1",
     *             "name" : "ASPhoto"
     *         },
     *         {
     *             "photo_pk" : "2",
     *             "name" : "패스파인더"
     *         },
     *     ]
     * }
     */
    @GetMapping
    public ResponseEntity getPhotoList(
            @Positive @RequestParam(value = "pk", required = false) Long pk,
            @RequestParam(value = "name", required = false) String name) {

        // 조회
        List<Photo> findPhotos = photoService.findPhotos(pk, name);

        // 응답
        List<PhotoResponseDto.Response> photos = photoMapper.photoToPhotoListResponseDto(findPhotos);
        JSONObject response = new JSONObject();
        response.put("photos", photos);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     *
     * @param photoPk
     * @param patch
     * @return {
     * 	"photo_pk" : 1,
     * 	"name" : "SAMSUNG"
     * }
     */
    @PatchMapping("/{photo_pk}")
    public ResponseEntity patchPhoto(@Positive @PathVariable("photo_pk") long photoPk,
                                       @Valid @RequestBody PhotoRequestDto.Patch patch) {
        // 수정
        patch.setPk(photoPk);
        Photo photo = photoService.patchPhoto(photoMapper.photoPatchDtoToPhoto(patch));

        // 응답
        PhotoResponseDto.Response response = photoMapper.photoToPhotoResponseDto(photo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param photoPk
     * @return {
     *     "photo": {
     *         "pk": 4,
     *         "name": "photo2",
     *         "delYn": true,
     *         "createdAt": "2023-08-28T16:45:47.017454",
     *         "modifiedAt": "2023-08-29T12:26:59.471178"
     *     },
     *     "message": "success delete"
     * }
     */
    @DeleteMapping("/{photo_pk}")
    public ResponseEntity deletePhoto(@Positive @PathVariable("photo_pk") long photoPk) {
        // 삭제
        Photo photo = photoService.deletePhoto(photoPk);

        // 응답
        PhotoResponseDto.Response photoResponse = photoMapper.photoToPhotoResponseDto(photo);
        JSONObject response = new JSONObject();
        response.put("photo", photoResponse);
        response.put("message", "delete successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}