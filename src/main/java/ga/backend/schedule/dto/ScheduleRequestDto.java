package ga.backend.schedule.dto;

import ga.backend.schedule.entity.Schedule;
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
        private String address;
        private Boolean meetYn;
        private boolean delYn;
        private String color;
        private String progress;
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
        private String address;
        private Boolean meetYn;
        private Boolean delYn;
        private String color;
        private Schedule.Progress progress;
    }
}
