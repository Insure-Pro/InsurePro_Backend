package ga.backend.dong.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class DongRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String dongName;
        private Boolean delYn;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String dongName;
        private Boolean delYn;
    }
}
