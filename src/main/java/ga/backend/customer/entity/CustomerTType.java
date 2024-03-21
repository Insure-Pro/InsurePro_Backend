package ga.backend.customer.entity;

import lombok.Getter;

@Getter
public enum CustomerTType {
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

    CustomerTType(String value) {
        this.value = value;
    }
}