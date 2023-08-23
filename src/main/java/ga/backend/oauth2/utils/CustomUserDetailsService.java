package ga.backend.oauth2.utils;

import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.BusinessLogicException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<Employee> optionalMember = employeeRepository.findByLoginId(loginId);
        Employee findEmployee = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.TOKEN_MEMBER_NOT_FOUND));

        return new CustomUserDetails(findEmployee);
    }


    private final class CustomUserDetails extends Token implements UserDetails {
        CustomUserDetails(Token token) {
            setId(token.getId());
            setAccessToken(token.getAccessToken());
            setRefreshToken(token.getRefreshToken());
            setLoginId(token.getLoginId());
            setLoginPw(token.getPassword());
            setPurchaseMember(token.getPurchaseMember());
            setSellMember(token.getSellMember());
            setRoles(token.getRoles());

        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getLoginId();
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
            return true;
        }
    }
}