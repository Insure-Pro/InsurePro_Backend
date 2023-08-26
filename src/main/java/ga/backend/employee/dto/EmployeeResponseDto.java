package ga.backend.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class EmployeeResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String id;
        private String email;
        private boolean regiYn;
        private boolean delYn;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
