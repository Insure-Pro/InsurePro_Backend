package ga.backend.oauth2.filter;

import com.umc.mot.utils.CustomCookie;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
    private final CustomCookie cookie;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Origin", "http://ec2-13-209-56-72.ap-northeast-2.compute.amazonaws.com:8080");
//        response.setHeader("Access-Control-Allow-Origin", "http://ec2-13-209-56-72.ap-northeast-2.compute.amazonaws.com");
//        response.setHeader("Access-Control-Allow-Origin", "https://seb41-main-022.vercel.app");
//        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//        response.addHeader("Access-Control-Allow-Origin", "http://localhost:8000");
        response.setHeader("Access-Control-Allow-Credentials", "true"); // 쿠키정책 허용
        response.setHeader("Access-Control-Allow-Methods",
                "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, POST, OPTIONS, PATCH");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, access-Token, refresh-Token, Authorization");
        response.setHeader("Access-Control-Expose-Headers",
                "access-Token, refresh-Token");

        // 로그인/회원가입의 버튼을 눌렀을 시, 쿠키값 저장하기
//        System.out.println("!! 필터 : " + request.getHeaderNames());
//        System.out.println("!! 필터2 : " + request.getHeader("host"));
//        Enumeration<String> attributes = ((HttpServletRequest) req).getHeaderNames();
//        while (attributes.hasMoreElements()) {
//            String attribute = (String) attributes.nextElement();
//            System.err.println(attribute+" : "+request.getSession().getAttribute(attribute));
//        }
        cookie.createCookie(request, response);

        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}