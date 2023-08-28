package ga.backend.dayschedule.dto;

import ga.backend.employee.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class DayScheduleRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private Employee employee;
        private String content; // 일정 내용
        private boolean doYn; // 달성 여부
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private Employee employee;
        private String content; // 일정 내용
        private boolean doYn; // 달성 여부
    }
}
