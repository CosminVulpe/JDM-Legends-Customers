package com.jdm.legends.customers.security.filters;

import com.jdm.legends.customers.security.service.JwtGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
    protected static final String AUTHORIZATION = "Authorization";
    protected static final String PREFIX_AUTHORIZATION = "BEARER ";

    private final JwtGeneratorService jwtGeneratorService;

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            SecretKey secretKey = jwtGeneratorService.computeSecretKey();
            String jwtToken = jwtGeneratorService.generateJwtToken(authentication, secretKey);

            response.setHeader(AUTHORIZATION, PREFIX_AUTHORIZATION + jwtToken);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/sign");
    }
}
