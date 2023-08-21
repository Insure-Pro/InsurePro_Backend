package ga.backend.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalTime;

public class PerformanceRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
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

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
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
