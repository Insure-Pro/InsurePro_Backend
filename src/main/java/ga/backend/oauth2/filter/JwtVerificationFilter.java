package ga.backend.oauth2.filter;

import ga.backend.employee.entity.Employee;
import ga.backend.employee.service.EmployeeService;
import ga.backend.exception.*;
import ga.backend.oauth2.jwt.JwtTokenizer;
import ga.backend.oauth2.utils.CustomAuthorityUtils;
import ga.backend.util.CustomCookie;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final EmployeeService employeeService;
    private final CustomCookie cookie;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("# JwtVerificationFilter");
        cookie.createCookie(request, response);

//        check(request);
        try {
            Map<String, Object> claims = verifyAuthorizationJws(request);
            setAuthenticationToContext(claims);
        } catch (SignatureException se) { // signature 에러
            request.setAttribute("exception", se);
        } catch (ExpiredJwtException ee) { // 기간 만료
            request.setAttribute("exception", ee);

            /*
                 access-Token의 유효기간이 만료되었을 때,
                 refresh-Token을 이용해서 access-Token 재발행
                 단, refresh-Token이 만료되면 재로그인 필요
            */

            verifyRefreshToken(request, response);

        } catch (Exception e) { // 그 외의 에러
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    // refresh 토큰 유효성 검사
    protected void verifyRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            // refresh 토큰 유효성 검사
            Map<String, Object> refreshClaims = verifyRefreshJws(request);
            Employee employee = verifyClaims(refreshClaims);

            // 새로운 AccessToken 발생 및 전달
//            String accessToken = delegateAccessTokenByRefreshToken(refreshClaims, token);
//            response.setHeader("Authorization", accessToken);
        } catch (SignatureException se) {
            request.setAttribute("exception", se);
            System.out.println("!! RefreshToken의 signiture와 payload가 불일치하면 동작");
        } catch (ExpiredJwtException eee) {
            request.setAttribute("exception", eee);
            System.out.println("!! RefreshToken 유효기간 종료");
        } catch (Exception e) {
            request.setAttribute("exception", e);
            System.out.println("!! RefreshToken의 header 불일치하면 동작");
        }
    }

    // access token 재발급 API를 위한 메서드
    public void assignAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // refresh 토큰 유효성 검사
        Map<String, Object> refreshClaims = verifyRefreshJws(request);
        Employee employee = verifyClaims(refreshClaims);

        // 새로운 AccessToken 발생 및 전달
        String accessToken = delegateAccessTokenByRefreshToken(refreshClaims, employee);
        response.setHeader("Authorization", accessToken);
    }

    // refresh-Token을 이용한 access-Token 재발행
    private String delegateAccessTokenByRefreshToken(Map<String, Object> refreshClaims, Employee employee) {
        // 새로운 Access 토큰 발행
        String subject = (String) refreshClaims.get("sub");
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", employee.getId());
        claims.put("roles", employee.getRoles());

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = "Bearer " + jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        // 새로 발행된 access-Token 업데이트
        employee.setAccessToken(accessToken);
        employeeService.patchEmployeeToken(employee);

        return accessToken;
    }

    // access token 발행
    public String delegateAccessToken(Employee employee) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", employee.getId()); // 사번
        claims.put("roles", employee.getRoles()); // 권한

        String subject = employee.getEmail(); // 이메일
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return "Bearer " + jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey); // access token
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer");
    }

    // access token 유효성 검증
    public Map<String, Object> verifyAuthorizationJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        verifyClaims(claims);
        return claims;
    }

    // refresh token 유효성 검증
    public Map<String, Object> verifyRefreshJws(HttpServletRequest request) {
        String jws = request.getHeader("Refresh");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        return claims;
    }

    // token의 값이 실제 DB에 존재하는지 검사
    public Employee verifyClaims(Map<String, Object> claims) {
        // token의 subject 유효성 검사
        String subject = (String) claims.get("sub"); // 이메일
        Employee employee = employeeService.verifiedEmployeeByEmail(subject);

        // token의 id 유효성 검사
        String id = (String) claims.get("id"); // 사번
        Employee employeeById = employeeService.verifiedEmployeeById(id);
        if(!employeeById.equals(employeeById)) throw new BusinessLogicException(ExceptionCode.TAMPERED_TOKEN);

        // token의 roles 유효성 검사
        if(claims.get("roles") != null) {
            List<String> roles = employee.getRoles();
            List<String> claimsRoles = (List) claims.get("roles");
            if (!roles.containsAll(claimsRoles)) throw new BusinessLogicException(ExceptionCode.TAMPERED_TOKEN);
        }

        return employee;
    }

    // contextholder에 세션 저장
    public void setAuthenticationToContext(Map<String, Object> claims) {
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(claims.get("sub"), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // access-Token을 이용해서 user 정보 조회하기
//    private void check(HttpServletRequest request) {
//        String token = request.getHeader("access-Token");
//        String newToken = (String) request.getAttribute("new-access-Token");
//        Optional<PurchaseMember> user = tokenService.findByToken(token);
//        Optional<PurchaseMember> newUser = tokenService.findByToken(newToken);
//
//        if(newToken == null) user.orElseThrow(() -> new BusinessLogicException(ExceptionCode.PURCHASE_MEMBER_NOT_FOUND));
//        else newUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.PURCHASE_MEMBER_NOT_FOUND));
//    }
}