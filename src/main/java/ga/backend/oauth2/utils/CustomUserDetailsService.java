package ga.backend.oauth2.utils;

import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final CustomAuthorityUtils authorityUtils;
    private boolean isSocialLogin = true;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee findEmployee;

        if(username.contains("@")) { // 로그인 - email & password
            Optional<Employee> optionalMember = employeeRepository.findByEmailAndDelYnFalse(username);
            findEmployee = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
            isSocialLogin = false;
        } else { // 소셜 로그인 - kakaoId & email
            Optional<Employee> optionalMember = employeeRepository.findByKakaoIdAndDelYnFalse(Integer.parseInt(username));
            findEmployee = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));
        }

        return new CustomUserDetails(findEmployee);
    }

    private final class CustomUserDetails extends Employee implements UserDetails {
        private final Employee employee;

        CustomUserDetails(Employee employee) {
            this.employee = employee;
            setPk(employee.getPk());
            setId(employee.getId());
            setKakaoId(employee.getKakaoId());
            setEmail(employee.getEmail());
            setPassword(employee.getPassword());
            setKakaoPw(employee.getKakaoPw());
            setDelYn(employee.getDelYn());
            setAccessToken(employee.getAccessToken());
            setRefreshToken(employee.getRefreshToken());
            setRoles(employee.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public String getPassword() {
            if(isSocialLogin) return getKakaoPw();
            return employee.getPassword();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return !getDelYn();
        }
    }
}