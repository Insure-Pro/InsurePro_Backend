package ga.backend.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class AnalysisRequestDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Post {

        private Long pk;

        @NotBlank(message = "회사명은 필수 입력 값입니다.")
        @Size(min=2, message = "회사명은 2자 이상이어야 합니다.")
        private String name; // 회사명

        private boolean delYn; // 회사 삭제 여부
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Get {
        private Long pk;
        private LocalDate date; // 확인 시간
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Patch {

        private Long pk;

        @NotBlank(message = "회사명은 필수 입력 값입니다.")
        @Size(min=2, message = "회사명은 2자 이상이어야 합니다.")
        private String name; // 회사명

        private boolean delYn; // 회사 삭제 여부

    }
}
