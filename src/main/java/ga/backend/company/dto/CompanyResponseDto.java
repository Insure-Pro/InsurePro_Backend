package ga.backend.company.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CompanyResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String name; // 회사명
        private boolean delYn; // 회사 삭제 여부
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
