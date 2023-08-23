package ga.backend.oauth2.handler;

import com.umc.mot.oauth2.filter.JwtVerificationFilter;
import com.umc.mot.token.entity.Token;
import com.umc.mot.token.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
public class MemberLogoutSuccessHandler implements LogoutSuccessHandler {
    private final TokenService tokenService;
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
        Token token = tokenService.getLoginToken();
        token.setAccessToken("");
        token.setRefreshToken("");
        tokenService.patchToken(token);
    }
}
