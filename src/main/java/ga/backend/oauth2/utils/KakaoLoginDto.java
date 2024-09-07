package ga.backend.oauth2.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoLoginDto {
    private Long kakaoId;
    private String email;
}
