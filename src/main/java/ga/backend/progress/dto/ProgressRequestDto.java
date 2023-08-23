package ga.backend.progress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ProgressRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String optionName;
        private boolean delYn;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String optionName;
        private boolean delYn;
    }
}
