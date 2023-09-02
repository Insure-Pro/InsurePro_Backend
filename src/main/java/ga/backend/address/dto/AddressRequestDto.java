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
        private int code;
        private String metro;
        private String gu;
        private String dong;
        private String li;
        private String latitude;
        private String longitude;
        private String codeType;
    }
}
