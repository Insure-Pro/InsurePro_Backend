package ga.backend.hide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class HideRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private long employeePk; // 직원 식별자
        private long customerTypePk; // 고객유형 식별자
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private long employeePk; // 직원 식별자
        private long customerTypePk; // 고객유형 식별자
    }
}
