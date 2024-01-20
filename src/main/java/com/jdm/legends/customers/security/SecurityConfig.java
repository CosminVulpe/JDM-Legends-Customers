package com.jdm.legends.customers.security;

import com.jdm.legends.customers.security.filters.CsrfCookieFilter;
import com.jdm.legends.customers.service.enums.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private static final int PASSWORD_ENCODER_STRENGTH = 10;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityContext().requireExplicitSave(false);

        // Protection of CSRF
        httpSecurity.csrf(config ->
                config.ignoringAntMatchers("/register-customer/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
        httpSecurity.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

        httpSecurity.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Route protection
        httpSecurity.authorizeHttpRequests(requests ->
//                        requests.antMatchers("/temporary-customer/hello").hasAnyAuthority(Roles.CLIENT.name())
                                requests.antMatchers("/reminder-email/**", "/register-customer/**").permitAll()
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults());

        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(PASSWORD_ENCODER_STRENGTH);
    }
}
