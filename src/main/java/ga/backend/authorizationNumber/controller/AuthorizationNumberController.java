package ga.backend.authorizationNumber.controller;

import ga.backend.authorizationNumber.dto.AuthorizationNumberRequestDto;
import ga.backend.authorizationNumber.dto.AuthorizationNumberResponseDto;
import ga.backend.authorizationNumber.entity.AuthorizationNumber;
import ga.backend.authorizationNumber.mapper.AuthorizationNumberMapper;
import ga.backend.authorizationNumber.service.AuthorizationNumberService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(Version.currentUrl + "/email")
@Validated
@AllArgsConstructor
public class AuthorizationNumberController {
    private final AuthorizationNumberService authorizationNumberService;
    private final AuthorizationNumberMapper authorizationNumberMapper;

    // CREATE
    @PostMapping
    public ResponseEntity postAuthorizationNumber(@Valid @RequestBody AuthorizationNumberRequestDto.Post post) {
        AuthorizationNumber authorizationNumber = authorizationNumberService.createAuthorizationNumber(authorizationNumberMapper.authorizationNumberPostDtoToAuthorizationNumber(post));
        AuthorizationNumberResponseDto.Response response = authorizationNumberMapper.authorizationNumberToAuthorizationNumberResponseDto(authorizationNumber);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/check")
    public ResponseEntity getAuthorizationNumber(@Valid @RequestBody AuthorizationNumberRequestDto.Check check) {
        AuthorizationNumber authorizationNumber = authorizationNumberService.checkAuthNum(authorizationNumberMapper.authorizationNumberCheckDtoToAuthorizationNumber(check));
        AuthorizationNumberResponseDto.Response response = authorizationNumberMapper.authorizationNumberToAuthorizationNumberResponseDto(authorizationNumber);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // UPDATE
//    @PatchMapping
//    public ResponseEntity patchAuthorizationNumber(@Valid @RequestBody AuthorizationNumberRequestDto.Check check) {
//        AuthorizationNumber authorizationNumber = authorizationNumberService.patchAuthorizationNumber(authorizationNumberMapper.authorizationNumberCheckDtoToAuthorizationNumber(check));
//        AuthorizationNumberResponseDto.Response response = authorizationNumberMapper.authorizationNumberToAuthorizationNumberResponseDto(authorizationNumber);
//
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

    // DELETE
    @DeleteMapping("/{email}")
    public ResponseEntity deleteAuthorizationNumber(@Positive @PathVariable("email") String email) {
        authorizationNumberService.deleteAuthorizationNumber(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
