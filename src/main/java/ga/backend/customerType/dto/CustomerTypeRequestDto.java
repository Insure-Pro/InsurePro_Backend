package ga.backend.customerType.dto;

import ga.backend.company.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

public class CustomerTypeRequestDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private Long company_pk;
        private String type;
        private String detail;
        private boolean delYn;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private Long company_pk;
        private String type;
        private String detail;
        private boolean delYn;
    }
}
