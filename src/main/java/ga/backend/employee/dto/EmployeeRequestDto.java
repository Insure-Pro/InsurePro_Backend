package ga.backend.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.OffsetDateTime;

public class EmployeeRequestDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Post {
        private Long pk;
        private String id;
        private String email;
        private String password;
        private boolean regiYn;
        private boolean delYn;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Patch {
        private Long pk;
        private String id;
        private String email;
        private String password;
        private boolean regiYn;
        private boolean delYn;
    }
}
