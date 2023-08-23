package ga.backend.company.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

public class CompanyRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String name; // 회사명
        private boolean delYn; // 회사 삭제 여부
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String name; // 회사명
        private boolean delYn; // 회사 삭제 여부

    }
}
