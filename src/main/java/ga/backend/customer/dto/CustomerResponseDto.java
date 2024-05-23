package ga.backend.customer.dto;

import ga.backend.customer.entity.ConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private CustomerType customerType; // 고객 유형
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
        private ConsultationStatus consultationStatus; // 상담현황
        private LocalDateTime consultationStatusModifiedAt; // 상담현황 수정일자
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    // 행정동 관련된 response
    @AllArgsConstructor
    @Setter
    @Getter
    public static class MetroGuDongResponse {
        private Long pk;
        private CustomerType customerType; // 고객 유형
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
        private ConsultationStatus consultationStatus; // 상담현황
        private LocalDateTime consultationStatusModifiedAt; // 상담현황 수정일자
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private MetroGuDong metroGuDong; // metro, gu, dong에 대한 이름
    }

    // 좌표값 관련 response
    @AllArgsConstructor
    @Setter
    @Getter
    public static class CoordinateResponse {
        private Long pk;
        private CustomerType customerType; // 고객 유형
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
        private ConsultationStatus consultationStatus; // 상담현황
        private LocalDateTime consultationStatusModifiedAt; // 상담현황 수정일자
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Coordinate coordinate; // 좌표 설정
    }

    // metro, gu, dong에 대한 이름
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class MetroGuDong {
        private String metroName;
        private String guName;
        private String dongName;
    }

    // metro, gu, dong에 대한 이름 & 좌표 설정
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Coordinate {
        private Double x;
        private Double y;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class CustomerType {
        private Long pk;
        private String name;
    }
}
