package ga.backend.oauth2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.service.EmployeeService;
import ga.backend.oauth2.jwt.JwtDelegate;
import ga.backend.oauth2.utils.KakaoLoginDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtAuthenticationKakaoFilter extends UsernamePasswordAuthenticationFilter {  // (1)
    private final AuthenticationManager authenticationManager;
    private final JwtDelegate jwtDelegate;
    private final EmployeeService employeeService;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoLoginDto loginDto = objectMapper.readValue(request.getInputStream(), KakaoLoginDto.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getKakaoId(), String.valueOf(loginDto.getKakaoId()));

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException  {
        Employee employee = (Employee) authResult.getPrincipal();

        String accessToken = "Bearer " + jwtDelegate.delegateAccessToken(employee);
        String refreshToken = jwtDelegate.delegateRefreshToken(employee);

        response.setHeader("Authorization", accessToken);
        response.setHeader("Refresh", refreshToken);

        // ------------------------------------------------------------
        // response.body에 내용 생성
        saveResponseBody(response, employee);

        // token에 Authorization과 Refresh 토큰값 설정하기
        employee.setAccessToken(accessToken);
        employee.setRefreshToken(refreshToken);
        employee = employeeService.patchEmployeeToken(employee);
        // ------------------------------------------------------------

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    private void saveResponseBody(HttpServletResponse response,
                                  Employee employee)  throws IOException {
        // response.body 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        // Token를 UserResponseDto로 변환
        MemberResopnse userResponse = new MemberResopnse(
                employee.getPk(),
                employee.getKakaoId(),
                employee.getEmail()
        );

        // json 형식으로 변환
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(userResponse);
        response.getWriter().write(result);
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public class MemberResopnse {
        private long pk; // 식별자
        private long KakaoId; // 사번
        private String email; // 로그인 아이디
    }
}