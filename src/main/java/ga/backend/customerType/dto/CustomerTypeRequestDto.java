package ga.backend.customerType.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CustomerTypeRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String name;
        private boolean delYn;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String type; // 고객 유형
        private String detail; // 설명
        private boolean delYn = false; // 고객 유형 삭제 여부

    }
}
