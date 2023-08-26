package ga.backend.dong.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class DongResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String name;
        private boolean delYn;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
