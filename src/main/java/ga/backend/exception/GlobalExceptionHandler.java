package ga.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
//    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicException.class);
//    private static final Logger logger = LoggerFactory.getLogger("Spring Security Debugger");

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex, HttpServletRequest request){
        log.error("BusinessLogicException", ex);

        int status = ex.getExceptionCode().getStatus();
        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String exception = ex.getClass().getName();
        String message = ex.getMessage();
        String path = request.getServletPath();

        ErrorResponse response = new ErrorResponse(status, error, exception, message, path);
//        logger.error("Response: {}", response);
//        logger.error("Business logic exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getExceptionCode().getStatus()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception ex){
//        log.error("handleException",ex);
//        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}