package ga.backend.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PhotoRequestDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String name;
        private String photoUrl;
        private String photoBinary;
        private Long employeePk;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String name;
        private String photoUrl;
        private Long employeePk;
    }
}
