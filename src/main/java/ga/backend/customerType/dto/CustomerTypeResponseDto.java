package ga.backend.customerType.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CustomerTypeResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String type;
        private String detail;
        private boolean delYn;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
