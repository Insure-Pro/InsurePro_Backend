package ga.backend.question.dto;

import ga.backend.employee.dto.EmployeeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class QuestionSlack {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String content;
        private String imageUrl;
        private LocalDateTime createdAt;
        private EmployeeResponseDto.SlackResponse employee;
    }
}
