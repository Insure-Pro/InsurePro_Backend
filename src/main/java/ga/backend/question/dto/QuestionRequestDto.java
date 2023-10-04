package ga.backend.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class QuestionRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;

        @NotBlank(message = "팀명은 필수 입력 값입니다.")
        @Size(min=2, message = "팀명은 2자 이상이어야 합니다.")
        private String teamName;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;

        @NotBlank(message = "팀명은 필수 입력 값입니다.")
        @Size(min=2, message = "팀명은 2자 이상이어야 합니다.")
        private String teamName;
    }
}
