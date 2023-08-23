package ga.backend.scheduleprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ScheduleProgressRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
    }
}
