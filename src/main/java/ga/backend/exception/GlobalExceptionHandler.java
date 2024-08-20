package ga.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex, HttpServletRequest request){
        log.error("BusinessLogicException", ex);

        int status = ex.getExceptionCode().getStatus();
        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String exception = ex.getClass().getName();
        String message = ex.getMessage();
        String path = request.getServletPath();

        ErrorResponse response = new ErrorResponse(status, error, exception, message, path);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getExceptionCode().getStatus()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request){
        String errorString = "Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.dao.DataIntegrityViolationException"+ex.getLocalizedMessage()+"] with root cause";
        log.error(errorString, ex);
//        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String exception = ex.getClass().getName();
        String message = ex.getCause().getCause() + "; " + ex.getMessage();
        String path = request.getServletPath();

        ErrorResponse response = new ErrorResponse(status, error, exception, message, path);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}