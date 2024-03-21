package ga.backend.analysis.dto;

import ga.backend.customer.entity.CustomerTType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
        private long TA; // 이번달 히스토리 TA 개수
        private long AP; // 이번달 히스토리 AP 개수
        private long PC; // 이번달 히스토리 PC 개수
        private int subscriptionCount; // 이번달 청약 개수
        private Double allTARatio; // 고객 all(AD+OD+CD+CP+JD)에 관한 이번달 히스토리 TA 비율
        private Double allAPRatio; // 고객 all(AD+OD+CD+CP+JD)에 관한 이번달 히스토리 AP 비율
        private Double allPCRatio; // 고객 all(AD+OD+CD+CP+JD)에 관한 이번달 히스토리 PC 비율
        private Double allHistoryRatio; // 이번달 분배만 받고 히스토리 아예없는 Db개수 /전체 이번달Db분배 받은 개수
        private LocalDate date; // 성과분석(년-월-01)
        private CustomerTType customerType; // 고객 유형
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
