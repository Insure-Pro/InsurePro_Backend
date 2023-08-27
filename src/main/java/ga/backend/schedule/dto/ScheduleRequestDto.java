package ga.backend.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String memo;
        private LocalDate date;
        private LocalTime startTm;
        private LocalTime finishTm;
        private LocalTime time;
        private String address;
        private boolean meetYn;
        private boolean delYn;
        private String color;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String memo;
        private LocalDate date;
        private LocalTime startTm;
        private LocalTime finishTm;
        private LocalTime time;
        private String address;
        private boolean meetYn;
        private boolean delYn;
        private String color;
    }
}
