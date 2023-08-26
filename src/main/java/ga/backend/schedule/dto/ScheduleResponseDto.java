package ga.backend.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ScheduleResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
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
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
