package ga.backend.customerType.dto;

import ga.backend.customerType.entity.DataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CustomerTypeResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private Long employeePk; // 관여한 사람
        private String name; // 고객유형 이름
        private String color; // 색상
        private String detail; // 상세내용
        private DataType dataType; // DB 유형
        private Integer asSetting; // AS 가능한 TA 횟수
        private Boolean delYn; // 고객 삭제 여부
        private LocalDateTime createdAt; // 생성일
        private LocalDateTime modifiedAt; // 수정일
    }
}
