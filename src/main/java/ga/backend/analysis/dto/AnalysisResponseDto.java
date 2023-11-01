package ga.backend.analysis.dto;

import ga.backend.customer.entity.Customer;
import ga.backend.util.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AnalysisResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk; // 식별자
        private Double TARatio; // 이번달 히스토리 TA 비율
        private Double APRatio; // 이번달 히스토리 AP 비율
        private Double PCRatio; // 이번달 히스토리 PC 비율
        private int subscriptionCount; // 이범달 청약 개수
        private LocalDate date; // 성과분석(년-월-01)
        private CustomerType customerType; // 고객 유형
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
