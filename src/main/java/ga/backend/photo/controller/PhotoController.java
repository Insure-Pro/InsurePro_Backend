package ga.backend.photo.controller;

import com.amazonaws.util.IOUtils;
import com.nimbusds.jose.shaded.json.JSONObject;
import ga.backend.employee.entity.Employee;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;


@RestController
@RequestMapping(Version.currentUrl + "/photos")
@Validated
@AllArgsConstructor
public class PhotoController {
    private final PhotoService photoService;
    private final PhotoMapper photoMapper;
    private final ImageService imageService;


    @RequestMapping(method=RequestMethod.POST, consumes="multipart/form-data")
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

    @PostMapping("/binary")
    public ResponseEntity postBinaryPhoto(@Valid @RequestBody PhotoRequestDto.Post post) throws IOException {

        // 이미지 업로드
        if (post.getPhotoBinary() != null) {
            Path currentPath = Paths.get("");
            String path = currentPath.toAbsolutePath().toString();
            byte[] data = DatatypeConverter.parseBase64Binary(post.getPhotoBinary());
            File file = new File(path + "/src/main/resources/photo/hello.jpg");
            try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                outputStream.write(data);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile2 = new MockMultipartFile("file",
                    file.getName(), "image/jpg", IOUtils.toByteArray(input));
            input.close();
            post.setPhotoUrl(imageService.updateImage(multipartFile2, "photo", "photoUrl"));

        }

        // 생성
        Photo photo = photoService.createPhoto(photoMapper.photoPostDtoToPhoto(post), post.getEmployeePk());

        // 응답
        PhotoResponseDto.Response response = photoMapper.photoToPhotoResponseDto(photo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    /**
     * @param employeePk
     * @return {
     * "photos" : [
     * {
     * "photo_pk" : "1",
     * "name" : "ASPhoto"
     * },
     * {
     * "photo_pk" : "2",
     * "name" : "패스파인더"
     * },
     * ]
     * }
     */
    @GetMapping
    public ResponseEntity getPhotoList(@Positive @RequestParam(value = "employee-pk", required = false) Long employeePk) {
        List<PhotoResponseDto.Response> response = photoService.findMyPhotoListByEmployee(employeePk);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/team")
    public ResponseEntity getPhotoListByTeam(
            @Positive @RequestParam(value = "employee-pk", required = false) Long employeepPk) {

        // 조회
        PhotoDetailResponseDto findMyPhoto = photoService.findMyPhotoByEmployee(employeepPk);
        List<PhotoDetailResponseDto> findTeamPhotoList = photoService.findTeamPhotoListByEmployee(employeepPk);

        // 응답
        JSONObject response = new JSONObject();
        response.put("myPlanner", findMyPhoto);
        response.put("myTeam", findTeamPhotoList);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * @param photoPk
     * @param patch
     * @return {
     * "photo_pk" : 1,
     * "name" : "SAMSUNG"
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
     * @param photoPk
     * @return {
     * "photo": {
     * "pk": 4,
     * "name": "photo2",
     * "delYn": true,
     * "createdAt": "2023-08-28T16:45:47.017454",
     * "modifiedAt": "2023-08-29T12:26:59.471178"
     * },
     * "message": "success delete"
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