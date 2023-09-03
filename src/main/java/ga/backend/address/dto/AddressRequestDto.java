package ga.backend.address.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class AddressRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private long code;
        private String metro;
        private String gu;
        private String dong;
        private String li;
        private double latitude;
        private double longitude;
        private String codeType;
    }
}
