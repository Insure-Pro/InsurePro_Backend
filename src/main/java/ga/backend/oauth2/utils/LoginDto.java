package ga.backend.oauth2.utils;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class LoginDto {
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=?<>:])[A-Za-z\\d~!@#$%^&*()+|=]{4,16}$",
            message = "특수문자는 1개 이상 들어가야 합니다, 비밀번호 '최소 4자에서 최대 16자'까지 허용")
    private String password;
}
