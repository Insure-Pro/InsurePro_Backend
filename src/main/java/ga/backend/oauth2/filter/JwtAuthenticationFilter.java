package ga.backend.oauth2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.mot.oauth2.jwt.JwtTokenizer;
import com.umc.mot.token.dto.LoginDto;
import com.umc.mot.token.entity.Token;
import com.umc.mot.token.service.TokenService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {  // (1)
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenizer jwtTokenizer,
                                   TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        this.tokenService = tokenService;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("!! attemptAuthentication");

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
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getLoginPw());

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
        Token token = (Token) authResult.getPrincipal();

        String accessToken = "Bearer " + delegateAccessToken(token);
        String refreshToken = delegateRefreshToken(token);

        response.setHeader("Authorization", accessToken);
        response.setHeader("Refresh", refreshToken);

        // ------------------------------------------------------------
        // response.body에 내용 생성
        saveResponseBody(response, token);

        // token에 Authorization과 Refresh 토큰값 설정하기
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        tokenService.patchToken(token);
        // ------------------------------------------------------------

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    private void saveResponseBody(HttpServletResponse response,
                                  Token token)  throws IOException {
        System.out.println("!! saveResponseBody");

        // response.body 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        // Token를 UserResponseDto로 변환
        MemberResopnse userResponse = new MemberResopnse(
                token.getPurchaseMember() != null ? token.getPurchaseMember().getName() : token.getSellMember().getName(), // 이름
                token.getLoginId() // 아이디
        );

        // json 형식으로 변환
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(userResponse);
        response.getWriter().write(result);
    }

    // access token 발급
    private String delegateAccessToken(Token token) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", token.getRoles());

        String subject = token.getLoginId();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey); // access token
    }

    // refresh token 발급
    private String delegateRefreshToken(Token token) {
        String subject = token.getLoginId();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey); // refresh token
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public class MemberResopnse {
        private String name; // 이름
        private String LoginId; // 로그인 아이디
    }
}