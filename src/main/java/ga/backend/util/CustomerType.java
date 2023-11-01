package ga.backend.util;

import lombok.Getter;

@Getter
public enum CustomerType {
    OD("OD"),
    AD("AD"),
    CP("CP"),
    CD("CD"),
    JD("JD"),
    H("H"),
    X("X"),
    Y("Y"),
    Z("Z");

    private final String value;

    CustomerType(String value) {
        this.value = value;
    }
}