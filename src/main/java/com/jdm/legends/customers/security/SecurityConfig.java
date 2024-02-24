package com.jdm.legends.customers.security;

import com.jdm.legends.customers.cors.ConfigGlobalCors;
import com.jdm.legends.customers.security.filters.CsrfCookieFilter;
import com.jdm.legends.customers.security.filters.JwtTokenGeneratorFilter;
import com.jdm.legends.customers.security.filters.JwtTokenValidationFilter;
import com.jdm.legends.customers.security.service.JwtGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.jdm.legends.customers.service.enums.RolesType.CLIENT;
import static com.jdm.legends.customers.service.enums.RolesType.POTENTIAL_CLIENT;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private static final int PASSWORD_ENCODER_STRENGTH = 10;
    private final JwtGeneratorService jwtGeneratorService;
    private final ConfigGlobalCors cors;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Protection of CSRF
        httpSecurity.csrf(config ->
                config.ignoringAntMatchers("/register-customer/**", "/temporary-customer/**", "/customer/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

        httpSecurity.cors(securityCorsConfigurer -> securityCorsConfigurer.configurationSource(cors.corsConfigurationSource()));

        // Add custom filters
        httpSecurity.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);
        httpSecurity.addFilterAfter(new JwtTokenGeneratorFilter(jwtGeneratorService), BasicAuthenticationFilter.class);
        httpSecurity.addFilterBefore(new JwtTokenValidationFilter(jwtGeneratorService), BasicAuthenticationFilter.class);

        httpSecurity.sessionManagement(configurer -> configurer.sessionCreationPolicy(STATELESS));

        // Route protection
        httpSecurity.authorizeHttpRequests(requests ->
                        requests.antMatchers("/sign").authenticated()
                                .antMatchers("/reminder-email/*").hasAnyRole(CLIENT.name(), POTENTIAL_CLIENT.name())
                                .antMatchers("/register-customer/**", "/temporary-customer/**", "/reminder-email/all").permitAll()
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
