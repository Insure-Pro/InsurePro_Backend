package ga.backend.authorizationNumber.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class AuthorizationNumberResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private String email;
        private int authNum; // 인증번호
    }
}
