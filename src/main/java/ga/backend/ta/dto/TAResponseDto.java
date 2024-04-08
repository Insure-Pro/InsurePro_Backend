package ga.backend.ta.dto;

import ga.backend.util.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class TAResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        @Size(min = 1, max = 20, message = "1에서 20 사이 숫자만 가능합니다.")
        private Integer count ; // 전화 횟수(1 ~ 20)
        private LocalDateTime time; // 전화환 시간
        private String memo; // 메모
        private Status status; // 상태(부재, 거절, 확약, AS대상)
        private boolean delYn; // 삭제여부
        private LocalDateTime createdAt; // 생성일
        private LocalDateTime modifiedAt; // 수정일
    }
}
