package ga.backend.oauth2.utils;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class LoginDto {
    private String email;
    private String password;
}
