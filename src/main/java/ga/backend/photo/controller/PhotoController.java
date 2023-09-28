package ga.backend.photo.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import ga.backend.photo.dto.PhotoDetailResponseDto;
import ga.backend.photo.dto.PhotoRequestDto;
import ga.backend.photo.dto.PhotoResponseDto;
import ga.backend.photo.entity.Photo;
import ga.backend.photo.mapper.PhotoMapper;
import ga.backend.photo.service.PhotoService;
import ga.backend.s3.service.ImageService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final ImageService imageService;

    /**
     *
     * @param post
     * @return {
     * 	"photo_pk" : 1,
     * 	"name" : "패스파인더"
     * }
     */
    @PostMapping
    public ResponseEntity postPhoto(@RequestPart(value = "request") PhotoRequestDto.Post post,
                                    @RequestPart(value = "image", required = false) MultipartFile file) {

        // 이미지 업로드
        if (file != null) {
            post.setPhotoUrl(imageService.updateImage(file, "photo", "photoUrl"));
        }

        // 생성
        Photo photo = photoService.createPhoto(photoMapper.photoPostDtoToPhoto(post), post.getEmployeePk());

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

    @GetMapping("/team")
    public ResponseEntity getPhotoListByTeam(
            @Positive @RequestParam(value = "employee-pk", required = false) Long employeepPk) {

        // 조회
        List<PhotoDetailResponseDto> findPhotos = photoService.findTeamPhotoListByEmployee(employeepPk);

        // 응답
//        List<PhotoResponseDto.Response> photos = photoMapper.photoToPhotoListResponseDto(findPhotos);
//        JSONObject response = new JSONObject();
//        response.put("photos", photos);

        return new ResponseEntity<>(findPhotos, HttpStatus.CREATED);
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
                                     @RequestPart(value = "request") PhotoRequestDto.Patch patch,
                                     @RequestPart(value = "image", required = false) MultipartFile file) {
        if (file != null) {
            patch.setPhotoUrl(imageService.updateImage(file, "photo", "photoUrl"));
        }

        // 수정
        patch.setPk(photoPk);
        Photo photo = photoService.patchPhoto(photoMapper.photoPatchDtoToPhoto(patch), patch.getEmployeePk());

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