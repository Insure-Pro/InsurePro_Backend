package ga.backend.util;

import lombok.Getter;

@Getter
public enum Status {
    ABSENCE("부재"),
    PENDING("보류"),
    REJECTION("거절"),
    PROMISE("확약"),
    AS_TARGET("AS 대상");

    private final String value;

    Status(String value) {
        this.value = value;
    }
}