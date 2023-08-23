
package ga.backend.exception;
import lombok.Getter;

public enum ExceptionCode {
    COMPANY_NOT_FOUND(404, "Company not found"),
    EMPLOYEE_NOT_FOUND(404, "Employee not found"),
    PROGRESS_NOT_FOUND(404, "Progress not found"),
    PERFORMANCE_NOT_FOUND(404, "Performance not found"),
    CUSTOM_TYPE_NOT_FOUND(404, "CustomType not found");

    @Getter
    private double status;

    @Getter
    private String message;

    ExceptionCode(double code, String message) {
        this.status = code;
        this.message = message;
    }
}
