package ga.backend.gu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class GuRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String gu;
        private Boolean delYn;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String gu;
        private Boolean delYn;
    }
}
