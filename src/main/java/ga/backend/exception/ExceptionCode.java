
package ga.backend.exception;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum ExceptionCode {
    COMPANY_NOT_FOUND(404, "Company not found"),
    CONTRACT_NOT_FOUND(404, "Contract not found"),
    CUSTOMERTYPE_NOT_FOUND(404, "CustomerType not found"),
    TEAM_NOT_FOUND(404, "Team not found"),
    TA_NOT_FOUND(404, "TA not found"),
    HIDE_NOT_FOUND(404, "Hide not found"),
    QUESTION_NOT_FOUND(404, "Question not found"),
    METRO_NOT_FOUND(404, "Metro not found"),
    GU_NOT_FOUND(404, "Gu not found"),
    DONG_NOT_FOUND(404, "Dong not found"),
    LI_NOT_FOUND(404, "Li not found"),
    SCHEDULE_NOT_FOUND(404, "Schedule not found"),
    CUSTOMER_NOT_FOUND(404, "Customer not found"),
    EMPLOYEE_NOT_FOUND(404, "Employee not found"),
    PROGRESS_NOT_FOUND(404, "Progress not found"),
    PERFORMANCE_NOT_FOUND(404, "Performance not found"),
    ANALYSIS_NOT_FOUND(404, "Ananlysis not found"),
    AUTHORIZATION_NUMBER_NOT_FOUND(404, "Authorization Number not found"),
    CUSTOMER_AGE_FILTER_NOT_FOUND(404, "requet age is not found"),
    PASSWORD_AND_REPASSWORD_NOT_SAME(400, "password and rePassword are not same"),
    INVALID_EMAIL(401, "Invalid Email"),
    WRONG_PASSWORD(401, "Wrong Password"),
    EMPLOYEE_NOT_CONTAIN_CUSTOMER(401, "Emploee not contains customer"),
    CUSTOMER_AND_EMPLOYEE_NOT_MATCH(403, "Customer's Employee & JWT Employee Is Not Match"),
    SCHEDULE_AND_EMPLOYEE_NOT_MATCH(403, "Schedule and Employee is not match"),
    SCHEDULE_AND_CUSTOMER_NOT_MATCH(403, "Schedule and Customer is not match"),
    EMPLOYEE_AND_CUSTOMERTYPE_NOT_MATCH(403, "Employee can't use this CustomerType"),
    EMPOYEE_AND_COMPANY_NOT_MATCH(403, "Employee can't use this Company"),
    CUSTOMERTYP_IS_USED(403, "CustomerType is used"),
    HIDE_ALREADY_EXITS(409, "Hide already exits"),
    TAMPERED_TOKEN(1004, "Tampered Token"), // 변조된 토큰
    FAIL_SEND_EMAIL(500, "Fail send email"),

    IMAGE_UPLOAD_FAIL(400, "이미지 업로드를 실패하였습니다."),
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
