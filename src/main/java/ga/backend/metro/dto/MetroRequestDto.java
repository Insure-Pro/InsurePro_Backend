package ga.backend.metro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class MetroRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String metroName;
        private Boolean delYn;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String metroName;
        private Boolean delYn;
    }
}
