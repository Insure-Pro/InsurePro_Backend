package ga.backend.customer.entity;

import lombok.Getter;

@Getter
public enum Gender {
    // 상담전, 상담보류중, 상품제안중, 병력대기, 청약거절, 상담거절, AS대상
    FEMALE("여성"),
    MALE("남성"),
    OTHER("기타");

    private final String value;

    Gender(String value) {
        this.value = value;
    }
}

