package ga.backend.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CustomerResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String name; // 이름
        private String birth; // 생년월일
        private int age; // 나이
        private String address; // 주소
        private String phone; // 연락처
        private String memo; // 메모
        private boolean contractYn; // 계약 체결 여부
        private boolean delYn; // 고객 삭제 여부
    }
}
