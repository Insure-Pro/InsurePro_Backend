package ga.backend.oauth2.handler;

import ga.backend.employee.entity.Employee;
import ga.backend.employee.service.EmployeeService;
import ga.backend.oauth2.filter.JwtVerificationFilter;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class MemberLogoutSuccessHandler implements LogoutSuccessHandler {
    private final EmployeeService employeeService;
    private final FindEmployee findEmployee;
    private final JwtVerificationFilter jwtVerificationFilter;


//    public MemberLogoutSuccessHandler(TokenService tokenService) {
//        this.tokenService = tokenService;
//    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        System.out.println("!! success logout");

        // 세션에 토큰 저장
        Map<String, Object> claims = jwtVerificationFilter.verifyAuthorizationJws(request);
        jwtVerificationFilter.setAuthenticationToContext(claims);

        // 토큰값 초기화
        Employee employee = findEmployee.getLoginEmployeeByToken();
        employee.setAccessToken("");
        employee.setRefreshToken("");
        employeeService.patchEmployeeToken(employee);
    }
}
