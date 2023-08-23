package ga.backend.customerType.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CustomerTypeResponseDto {
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private Long pk;
        private String name;
        private boolean delYn;
    }
}
