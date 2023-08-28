package ga.backend.authorizationNumber.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

public class AuthorizationNumberRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private long employee_pk;
        @Email
        private String email;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Check {
        private String email;
        private int authNum; // 인증번호
    }
}
