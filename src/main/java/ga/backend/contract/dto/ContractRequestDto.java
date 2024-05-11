package ga.backend.contract.dto;

import ga.backend.employee.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

public class ContractRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String name; // 보험 상품 이름
        private String memo; // 메모
        private LocalDate contractDate; // 계약 체결 날짜
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String name; // 보험 상품 이름
        private String memo; // 메모
        private LocalDate contractDate; // 계약 체결 날짜

        private Long customerPk;
        private Long schedulePk;
    }
}
