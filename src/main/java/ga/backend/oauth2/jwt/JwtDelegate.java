package ga.backend.oauth2.jwt;

import ga.backend.employee.entity.Employee;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class JwtDelegate {
    private final JwtTokenizer jwtTokenizer;

    // access token 발급
    public String delegateAccessToken(Employee employee) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", employee.getId()); // 사번
        claims.put("roles", employee.getRoles()); // 권한

        String subject = employee.getEmail(); // 이메일
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey); // access token
    }

    // refresh token 발급
    public String delegateRefreshToken(Employee employee) {
        String subject = employee.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey); // refresh token
    }
}
