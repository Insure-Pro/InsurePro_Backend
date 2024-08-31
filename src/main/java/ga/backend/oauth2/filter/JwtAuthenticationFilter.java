package ga.backend.oauth2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.backend.employee.entity.Employee;
import ga.backend.employee.service.EmployeeService;
import ga.backend.oauth2.jwt.JwtDelegate;
import ga.backend.oauth2.jwt.JwtTokenizer;
import ga.backend.oauth2.utils.LoginDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {  // (1)
    private final AuthenticationManager authenticationManager;
    private final JwtDelegate jwtDelegate;
    private final EmployeeService employeeService;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // employee test
//        Employee employee = new Employee();
//        employee.setPassword("1234aAcd!");
//        employee.setEmail("utest@gmail.com");
//        employee.setId("testId123");
//        employee = employeeService.createEmployee(employee);
//        System.out.println("!! pk : " + employeeService.verifiedEmployeeByPk((long)1).getEmail());
//        System.out.println("!! id : " + employeeService.verifiedEmployeeById("testId123").getEmail());
//
        System.out.println("##!! test");

        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

//        System.out.println("!! make objectMapper");
//        System.out.println("!! inputStream : " + request.getInputStream().readAllBytes());
//        System.out.println("!! " + getBody(request));
//        String result = getBody(request);
//        String[] results = result.split("\"");
//        for(int i=0; i<results.length; i++)
//            System.out.println(i+") " + results[i]);
//
//        LoginDto loginDto = new LoginDto();
//        try {
//            loginDto.setEmail(results[3]);
//            loginDto.setPassword(results[7]);
//        } catch (Exception e) {
////            double num = Math.random();
//            loginDto.setEmail("test@gamil.com");
//            loginDto.setPassword("q1q1Q!Q!");
//            User user = new User();
//            user.setEmail(loginDto.getEmail());
//            user.setPassword(loginDto.getPassword());
//            user.setName("test");
//            userService.createUser(user);
//        }
//        System.out.println("!! email : " + loginDto.getEmail());
//        System.out.println("!! pw : " + loginDto.getPassword());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

//    public String getBody(HttpServletRequest request) throws IOException {
//
//        String body = null;
//        StringBuilder stringBuilder = new StringBuilder();
//        BufferedReader bufferedReader = null;
//
//        try {
//            InputStream inputStream = request.getInputStream();
//            if (inputStream != null) {
//                System.out.println("!! inputStream");
//                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                char[] charBuffer = new char[128];
//                int bytesRead = -1;
//                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                    System.out.println("!1 char Buffer : " + charBuffer);
//                    stringBuilder.append(charBuffer, 0, bytesRead);
//                }
//            }
//        } catch (IOException ex) {
//            throw ex;
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException ex) {
//                    throw ex;
//                }
//            }
//        }
//
//        body = stringBuilder.toString();
//        return body;
//    }

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
        System.out.println("!! saveResponseBody");

        // response.body 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        // Token를 UserResponseDto로 변환
        MemberResopnse userResponse = new MemberResopnse(
                employee.getPk(),
                employee.getId(),
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
        private String id; // 사번
        private String email; // 로그인 아이디
    }
}