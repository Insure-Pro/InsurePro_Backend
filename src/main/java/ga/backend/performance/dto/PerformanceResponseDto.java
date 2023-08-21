package ga.backend.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

public class PerformanceResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private LocalDate date;
        private LocalTime startTm;
        private LocalTime finishTm;
        private int ta;
        private int introduction;
        private int rp;
        private int apc;
        private int contractNm;
    }
}
