package ga.backend.customerType.dto;

import ga.backend.customerType.entity.DataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

public class CustomerTypeRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        @Pattern(regexp = "^[a-zA-Z]{1,10}$",
                message = "영문자로만 설정 가능하며, 길이는 '최소 1자에서 최대 160'까지 허용")
        private String name; // 고객유형 이름
        private String color; // 색상
        private String detail; // 상세내용
        private DataType dataType; // DB 유형
        private boolean delYn = false; // 고객 삭제 여부
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        @Pattern(regexp = "^[a-zA-Z]{1,10}$",
                message = "영문자로만 설정 가능하며, 길이는 '최소 1자에서 최대 160'까지 허용")
        private String name; // 고객유형 이름
        private String color; // 색상
        private String detail; // 상세내용
        private DataType dataType; // DB 유형
        private Boolean delYn; // 고객 삭제 여부
    }
}
