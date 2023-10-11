package ga.backend.analysis.dto;

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
        private Double allTAPercentage; // 전체 대비 TA 확률
        private Double allAPPercentage; // 전체 대비 AP 확률
        private Double allPCPercentage; // 전체 대비 PC 확률
        private Double TAPercentage; // TA 진행확률
        private Double APPercentage; // AP 진행확률
        private Double PCPercentage; // PC 진행확률
        private Double allTARatio; // 전체 히스토리 TA 비율
        private Double allAPRatio; // 전체 히스토리 AP 비율
        private Double allPCRatio; // 전체 히스토리 PC 비율
        private Double subscriptionPercentage; // 청약확률
        private LocalDate date; // 성과분석(년-월-01)
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
