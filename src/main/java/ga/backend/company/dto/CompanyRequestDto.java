package ga.backend.company.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

public class CompanyRequestDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;

        @NotEmpty
        private String name; // 회사명

        @Column(insertable=false, updatable=false)
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
