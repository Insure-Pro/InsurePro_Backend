package ga.backend.customer.dto;

import ga.backend.customer.entity.ConsultationStatus;
import ga.backend.customer.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class CustomerRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long customerTypePk; // 고객 유형 식별자
        private String customerTypeName; // 고객 유형 이름
        private long liPk; // 리 식별자
        private String name; // 이름
        private LocalDate birth; // 생년월일
        private Integer age; // 나이
        private String dongString; // 행정동 주소
        private String address; // 상세주소
        @Pattern(regexp = "^010-?([0-9]{4})-?([0-9]{4})$",
                message = "전화번호 형태는 010-1234-1234 입니다.")
        private String phone; // 연락처
        private String memo; // 메모
        private String state; // 인수상태
        private Boolean contractYn; // 계약 완료 여부
        private LocalDate registerDate; // 고객 등록 날짜
        private MetroGuDong metroGuDong; // metro, gu, dong에 대한 이름 설정
        private ConsultationStatus consultationStatus; // 상담현황
        private String email; // 이메일
        private String work; // 직업
        private String worry; // 돈관련가장큰고민
        private String salary; // 평균 세전 월소득액(만)
        private String workTime; // 통화가능한 시간
        private String gender; // 성별
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private Long customerTypePk; // 고객 유형 식별자
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
        private ConsultationStatus consultationStatus; // 상담현황
        private String email; // 이메일
        private String work; // 직업
        private String worry; // 돈관련가장큰고민
        private String salary; // 평균 세전 월소득액(만)
        private String workTime; // 통화가능한 시간
        private Gender gender; // 성별
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