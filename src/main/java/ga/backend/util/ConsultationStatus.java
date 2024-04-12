package ga.backend.util;

import lombok.Getter;

@Getter
public enum ConsultationStatus {
    // 상담보류중, 상품제안중, 병력대기, 청약거절, 상담거절, AS대상
    PENDING_CONSULTATION("상담보류중"),
    PRODUCT_PROPOSAL("상품제안중"),
    MEDICAL_HISTORY_WAITING("병력대기"),
    SUBSCRIPTION_REJECTION("청약거절"),
    CONSULTATION_REJECTION("상담거절"),
    AS_TARGET("AS대상");

    private final String value;

    ConsultationStatus(String value) {
        this.value = value;
    }}
