package ga.backend.customer.dto;

import ga.backend.customer.entity.CustomerTType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class CustomerRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private CustomerTType customerTType; // 고객 유형
        private long liPk; // 리 식별자
        private String name; // 이름
        private LocalDate birth; // 생년월일
        private int age; // 나이
        private String dongString; // 행정동 주소
        private String address; // 상세주소
        @Pattern(regexp = "^010-?([0-9]{4})-?([0-9]{4})$",
                message = "전화번호 형태는 010-1234-1234 입니다.")
        private String phone; // 연락처
        private String memo; // 메모
        private String state; // 인수상태
        private Boolean contractYn;
        private LocalDate registerDate; // 고객 등록 날짜
        private MetroGuDong metroGuDong; // metro, gu, dong에 대한 이름 설정
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private CustomerTType customerTType; // 고객 유형
        private long liPk; // 리 식별자
        private String name; // 이름
        private LocalDate birth; // 생년월일
        private int age; // 나이
        private String dongString; // 행정동 주소
        private String address; // 상세주소
        @Pattern(regexp = "^010-?([0-9]{4})-?([0-9]{4})$",
                message = "전화번호 형태는 010-1234-1234 입니다.")
        private String phone; // 연락처
        private String memo; // 메모
        private String state; // 인수상태
        private Boolean contractYn; // 계약 체결 여부
        private Boolean delYn; // 고객 삭제 여부
        private LocalDate registerDate; // 고객 등록 날짜
        private MetroGuDong metroGuDong; // metro, gu, dong에 대한 이름 설정
    }

    // metro, gu, dong에 대한 이름 설정
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class MetroGuDong {
        private String metroName;
        private String guName;
        private String dongName;
    }
}