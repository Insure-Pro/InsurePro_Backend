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
        private LocalDate date; // 성과분석(년-월-01)
        private int dbCustomerCount; // 이번달에 추가된 customer 개수 - DB 고객유형
        private int etcCustomerCount; // 이번달에 추가된 customer 개수 - ETC 고객유형
        private CustomerConsultationRatio customerConsultationRatio; // customer의 상담현황 확률
        private int contractCount; // Contract의 계약 체결한 Contract 개수
        private int asTargetCount; // Customer의 상담현황 = AS_TARGET인 Customer 개수
        private TACustomerCount taCustomerCount; // TA의 Customer 개수
        private ScheduleCustomerCount scheduleCustomerCount; // Schedule의 Progress(진척도)의 Customer 개수
    }

    // customer의 상담현황 확률
    @AllArgsConstructor
    @Setter
    @Getter
    public static class CustomerConsultationRatio {
        private double beforeConsultationRatio; // 상담전
        private double pendingCounsultationRatio; // 상담보류중
        private double productProposalRatio; // 상품제안중
        private double medicalHistoryWaitingRatio; // 병력대기
        private double subscriptionRejectionRatio; // 청약거절
        private double consultationRejectionRatio; // 상담거절
    }

    // TA의 Customer 개수
    @AllArgsConstructor
    @Setter
    @Getter
    public static class TACustomerCount {
        private int absenceCount; // 부재
        private int rejectionCount; // 거절
        private int pendingCount; // 보류
        private int promiseCount; // 확약
    }

    // Schedule의 Progress(진척도)의 Customer 개수
    @AllArgsConstructor
    @Setter
    @Getter
    public static class ScheduleCustomerCount {
        private int apCount; // 초회상담
        private int icCount; // 정보수집
        private int pcCount; // 상품제안
        private int cicCount; // 계약체결
        private int stCount; // 증권전달
        private int ocCount; // 기타상담
    }
}
