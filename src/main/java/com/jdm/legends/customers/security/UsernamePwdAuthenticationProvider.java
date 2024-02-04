package com.jdm.legends.customers.security;


import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.entity.Role;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String PREFIX_ROLE = "ROLE_";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String emailAddress = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Customer customer = customerRepository.findCustomerByEmailAddress(emailAddress)
                .orElseThrow(() -> new EntityNotFoundException("No customer registered with this details"));

        if (!passwordEncoder.matches(pwd, customer.getPwd())) {
            String invalidPasswordMdg = "Invalid password";
            log.error(invalidPasswordMdg);
            throw new BadCredentialsException(invalidPasswordMdg);
        }

        List<GrantedAuthority> authorities = new ArrayList<>(
                getAuthorities(customer.getRole())
        );
        return new UsernamePasswordAuthenticationToken(emailAddress, pwd, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Role roles) {
        return roles.getRolesType().stream().map(role -> new SimpleGrantedAuthority(PREFIX_ROLE + role.name())).toList();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
