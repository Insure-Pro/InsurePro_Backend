package ga.backend.progress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ProgressResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String option;
        private boolean delYn;
    }
}
