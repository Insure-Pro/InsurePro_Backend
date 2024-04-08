package ga.backend.customerType.entity;

import lombok.Getter;

@Getter
public enum DataType {
    DB("DB"), // DB 유형
    ETC("ETC"); // 그 외의 유형

    private final String value;

    DataType(String value) {
        this.value = value;
    }
}