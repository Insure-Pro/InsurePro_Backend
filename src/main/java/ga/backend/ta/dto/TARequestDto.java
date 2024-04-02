package ga.backend.ta.dto;

import ga.backend.util.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class TARequestDto {
    @AllArgsConstructor
    @Valid
    @Setter
    @Getter
    public static class Post {
        private long customerPk; // 고객 식별자

        @Min(value = 1, message = "최소 1 이상이어야 합니다.")
        @Max(value = 20, message = "최대 20 이하여야 합니다.")
        private Integer count ; // 전화 횟수(1 ~ 20)

        private LocalDateTime time; // 전화환 시간
        private String memo; // 메모
        private Status status; // 상태(부재, 거절, 확약, AS대상)
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        @Min(value = 1, message = "최소 1 이상이어야 합니다.")
        @Max(value = 20, message = "최대 20 이하여야 합니다.")
        private Integer count ; // 전화 횟수(1 ~ 20)
        private LocalDateTime time; // 전화환 시간
        private String memo; // 메모
        private Status status; // 상태(부재, 거절, 확약, AS대상)
        private Boolean delYn; // 삭제여부
    }
}
