package ga.backend.li.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

public class LiRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String li; // 리 이름
        private double latitude; // 위도
        private double longitude; // 경도
        private Long dongPk; // 읍면동 식볇자
        private Boolean delYn = false;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String li; // 리 이름
        private double latitude; // 위도
        private double longitude; // 경도
    }
}
