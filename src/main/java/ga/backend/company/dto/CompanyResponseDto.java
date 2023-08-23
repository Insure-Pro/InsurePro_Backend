package ga.backend.company.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CompanyResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String name; // 회사명
        private boolean delYn; // 회사 삭제 여부

    }
}
