package ga.backend.scheduleprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ScheduleProgressResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
