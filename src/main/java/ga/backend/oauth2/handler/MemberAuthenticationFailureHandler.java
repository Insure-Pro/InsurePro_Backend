package ga.backend.oauth2.handler;

import com.google.gson.Gson;
import ga.backend.exception.ExceptionCode;
import ga.backend.oauth2.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        log.error("# Authentication failed: " + exception.getMessage());

        // 이메일 불일치
        if(exception.getMessage().equals("Employee not found")) sendErrorResponse(response, ExceptionCode.INVALID_EMAIL);
        // 비밀번호 불일치
        else sendErrorResponse(response, ExceptionCode.WRONG_PASSWORD);
    }

    private void sendErrorResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        Gson gson = new Gson();
        // 401, HttpStatus.UNAUTHORIZED
        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(exceptionCode.getStatus());

//        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }
}