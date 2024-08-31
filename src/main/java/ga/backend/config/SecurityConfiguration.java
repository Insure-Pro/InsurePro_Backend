package ga.backend.config;

import ga.backend.actuator.ApiUsageFilter;
import ga.backend.employee.service.EmployeeService;
import ga.backend.oauth2.filter.JwtAuthenticationFilter;
import ga.backend.oauth2.filter.JwtVerificationFilter;
import ga.backend.oauth2.handler.*;
import ga.backend.oauth2.jwt.JwtDelegate;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * JWT 검증 기능 추가
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {
    private final JwtDelegate jwtDelegate;
    private final EmployeeService employeeService;
    private final JwtVerificationFilter jwtVerificationFilter;
    private final MemberLogoutSuccessHandler memberLogoutSuccessHandler;
    private final ApiUsageFilter apiUsageFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("!! test");

        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
//                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .logout()
                .logoutUrl(Version.currentUrl + "/logout") // 로그아웃 처리 URL(기본값)
                .invalidateHttpSession(true) // 로그아웃 성공 시 세션 제거
                .clearAuthentication(true) // 로그아웃 시 권한 제거
                .permitAll() // 모두 허용
                .logoutSuccessHandler(memberLogoutSuccessHandler) // 로그아웃 성공 후 핸들러
                .and()
                .authorizeHttpRequests(authorize -> authorize // url authorization 전체 추가
//                                .antMatchers(HttpMethod.POST, "/*/coffees").hasRole("ADMIN")
//                                .antMatchers(HttpMethod.GET, "/*/coffees/**").hasAnyRole("USER", "ADMIN")
//                                .antMatchers(HttpMethod.GET, "/*/coffees").permitAll()
                                .anyRequest().permitAll()
                ).addFilterBefore(apiUsageFilter, UsernamePasswordAuthenticationFilter.class) // 커스텀 필터 추가

        ;

        return http.build();
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "DELETE"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    // 추가
    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            // 로그인
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtDelegate, employeeService);
            jwtAuthenticationFilter.setFilterProcessesUrl(Version.currentUrl + "/login");          // login url

            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            builder
                    .addFilter(jwtAuthenticationFilter) // 로그인
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class); // google OAuth2
        }
    }
}
