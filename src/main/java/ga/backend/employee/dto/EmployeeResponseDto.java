package ga.backend.employee.dto;

import ga.backend.team.dto.TeamResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class EmployeeResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String id; // 사번
        private Long KakaoId; // 카카오톡 ID
        private String email;
        private String name; // 이름
        private Long year; // 연차
        private TeamResponseDto.Response teamResponseDto;
        private boolean regiYn;
        private boolean delYn;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class SimpleResponse {
        private Long pk;
        private String id; // 사번
        private Long KakaoId; // 카카오톡 ID
        private String email;
        private String name; // 이름
        private Long year; // 연차
        private boolean regiYn;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class SlackResponse {
        private Long pk;
        private String email;
        private String name; // 이름
    }
}
