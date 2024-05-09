package ga.backend.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ContractResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String name; // 보험 상품 이름
        private String memo; // 메모
        private LocalDate contractDate; // 계약 체결 날짜
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
