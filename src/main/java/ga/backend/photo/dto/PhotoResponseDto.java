package ga.backend.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class PhotoResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String name;
        private String photoUrl;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
