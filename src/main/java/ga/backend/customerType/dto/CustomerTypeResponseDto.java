package ga.backend.customerType.dto;

import ga.backend.company.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CustomerTypeResponseDto {
    @Setter
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long pk;
        private String type;
        private String detail;
        private boolean delYn;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
