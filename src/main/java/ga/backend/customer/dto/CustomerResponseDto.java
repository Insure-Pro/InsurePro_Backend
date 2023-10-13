package ga.backend.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String customerTypeString; // 고객 유형
        private String name; // 이름
        private LocalDate birth; // 생년월일
        private int age; // 나이
        private String dongString; // 행정동 주소
        private String address; // 상세주소
        private String phone; // 연락처
        private String memo; // 메모
        private String state; // 인수상태
        private Boolean contractYn; // 계약 체결 여부
        private Boolean delYn; // 고객 삭제 여부
        private LocalDate registerDate; // 고객 등록 날짜
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
