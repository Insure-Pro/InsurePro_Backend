package ga.backend.exception;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessLogicException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicException.class);

    @Getter
    private ExceptionCode exceptionCode;

    @Getter
    private String message;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.message = exceptionCode.getMessage();
        this.exceptionCode = exceptionCode;
        logError();
    }

    public BusinessLogicException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.message = message;
        this.exceptionCode = exceptionCode;
        logError();
    }

    private void logError() {
        logger.error("Business logic exception occurred: " + this.exceptionCode + ", Message: " + this.message);
        // Optionally, log additional details or stack trace
    }
}
