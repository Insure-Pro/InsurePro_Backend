
package ga.backend.exception;
import lombok.Getter;

import java.util.Arrays;

public enum ExceptionCode {
    COMPANY_NOT_FOUND(404, "Company not found"),
    TEAM_NOT_FOUND(404, "Team not found"),
    METRO_NOT_FOUND(404, "Metro not found"),
    GU_NOT_FOUND(404, "Gu not found"),
    DONG_NOT_FOUND(404, "Dong not found"),
    LI_NOT_FOUND(404, "Li not found"),
    SCHEDULE_NOT_FOUND(404, "Schedule not found"),
    CUSTOMER_TYPE_NOT_FOUND(404, "CustomerType not found"),
    CUSTOMER_NOT_FOUND(404, "Customer not found"),
    EMPLOYEE_NOT_FOUND(404, "Employee not found"),
    PROGRESS_NOT_FOUND(404, "Progress not found"),
    PERFORMANCE_NOT_FOUND(404, "Performance not found"),
    CUSTOM_TYPE_NOT_FOUND(404, "CustomType not found"),
    AUTHORIZATION_NUMBER_NOT_FOUND(404, "Authorization Number not found"),
    PASSWORD_AND_REPASSWORD_NOT_SAME(400, "password and rePassword are not same"),
    INVALID_EMAIL(401, "Invalid Email"),
    WRONG_PASSWORD(401, "Wrong Password"),
    TAMPERED_TOKEN(1004, "Tampered Token"), // 변조된 토큰
    FAIL_SEND_EMAIL(500, "Fail send email"),
    NOT_MATCH_AUTHNUM(400, "Not Match authNum");


    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }

    public static ExceptionCode findByMessage(String message) {
        return Arrays.stream(ExceptionCode.values())
                .filter(exceptionCode -> exceptionCode.getMessage().equals(message))
                .findFirst()
                .orElse(null);
    }
}
