package ga.backend.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class TeamRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;

        @NotBlank(message = "팀명은 필수 입력 값입니다.")
        @Size(min=2, message = "팀명은 2자 이상이어야 합니다.")
        private String teamName;
        private long companyPk; // 회사 식별자
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;

        @NotBlank(message = "팀명은 필수 입력 값입니다.")
        @Size(min=2, message = "팀명은 2자 이상이어야 합니다.")
        private String teamName;
        private long companyPk; // 회사 식별자
    }
}
