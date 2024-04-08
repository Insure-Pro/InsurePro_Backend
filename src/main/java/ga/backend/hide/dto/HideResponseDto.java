package ga.backend.hide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class HideResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
//        private long employeePk; // 직원 식별자
//        private long customerTypePk; // 고객유형 식별자
    }
}
