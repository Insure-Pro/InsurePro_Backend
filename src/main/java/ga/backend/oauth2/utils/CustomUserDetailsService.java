package ga.backend.oauth2.utils;

import ga.backend.employee.entity.Employee;
import ga.backend.employee.repository.EmployeeRepository;
import ga.backend.exception.*;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> optionalMember = employeeRepository.findByEmailAndDelYnFalse(email);
        Employee findEmployee = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.EMPLOYEE_NOT_FOUND));

        return new CustomUserDetails(findEmployee);
    }


    private final class CustomUserDetails extends Employee implements UserDetails {
        CustomUserDetails(Employee employee) {
            setPk(employee.getPk());
            setId(employee.getId());
            setEmail(employee.getEmail());
            setPassword(employee.getPassword());
            setRegiYn(employee.getRegiYn());
            setDelYn(employee.getDelYn());
            setAccessToken(employee.getAccessToken());
            setRefreshToken(employee.getRefreshToken());
            setRoles(employee.getRoles());
            setCompany(employee.getCompany());
            setPerformances(employee.getPerformances());
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