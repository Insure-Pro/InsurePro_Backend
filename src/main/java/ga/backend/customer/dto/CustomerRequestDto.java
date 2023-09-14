package ga.backend.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class CustomerRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private long customerTypePk; // 고객 유형 식별자
        private long liPk; // 리 식별자
        private String name; // 이름
        private LocalDate birth; // 생년월일
        private int age; // 나이
        private String address; // 상세주소
        @Pattern(regexp = "^010-?([0-9]{4})-?([0-9]{4})$",
                message = "전화번호 형태는 010-1234-1234 입니다.")
        private String phone; // 연락처
        private String memo; // 메모
        private LocalDate intensiveCareStartDate; // 집중관리시기 - 시작
        private LocalDate intensiveCareFinishDate; // 집중관리시기 - 끝
        private LocalDate registerDate; // 고객 등록 날짜
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private long customerTypePk; // 고객 유형 식별자
        private long liPk; // 리 식별자
        private String name; // 이름
        private LocalDate birth; // 생년월일
        private int age; // 나이
        private String dongString; // 행정동 주소
        @Pattern(regexp = "^010-?([0-9]{4})-?([0-9]{4})$",
                message = "전화번호 형태는 010-1234-1234 입니다.")
        private String phone; // 연락처
        private String memo; // 메모
        private Boolean contractYn; // 계약 체결 여부
        private Boolean delYn; // 고객 삭제 여부
        private LocalDate intensiveCareStartDate; // 집중관리시기 - 시작
        private LocalDate intensiveCareFinishDate; // 집중관리시기 - 끝
        private LocalDate registerDate; // 고객 등록 날짜
    }
}
