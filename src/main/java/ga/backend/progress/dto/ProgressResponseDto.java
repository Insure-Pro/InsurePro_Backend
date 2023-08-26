package ga.backend.progress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProgressResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String optionName;
        private boolean delYn;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
