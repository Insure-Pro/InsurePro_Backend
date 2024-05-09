package ga.backend.schedule.entity;

import lombok.Getter;

@Getter
public enum Progress {
    // 초회상담(AP) / 상품제안(PC) / 증권전달(ST)
    AP("초회상담"),
    PC("상품제안"),
    ST("증권전달"),
    IC("정보수집"),
    OC("기타상담"),
    CIC("계약체결");

    private final String value;

    Progress(String value) {
        this.value = value;
    }
}