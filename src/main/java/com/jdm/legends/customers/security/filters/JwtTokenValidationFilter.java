package com.jdm.legends.customers.security.filters;

import com.jdm.legends.customers.security.service.JwtGeneratorService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jdm.legends.customers.security.filters.JwtTokenGeneratorFilter.AUTHORIZATION;
import static com.jdm.legends.customers.security.filters.JwtTokenGeneratorFilter.PREFIX_AUTHORIZATION;

@Configuration
@RequiredArgsConstructor
public class JwtTokenValidationFilter extends OncePerRequestFilter {
    private final JwtGeneratorService jwtGeneratorService;

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {
        String headerJWT = request.getHeader(AUTHORIZATION);

        if (headerJWT != null && headerJWT.startsWith(PREFIX_AUTHORIZATION) ) {
            try {
                SecretKey secretKey = jwtGeneratorService.computeSecretKey();
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(headerJWT.replace(PREFIX_AUTHORIZATION, ""))
                        .getBody();
                String username = (String) claims.get("username");
                String authorities = (String) claims.get("authorities");
                Authentication authentication = new UsernamePasswordAuthenticationToken(username
                        , null
                        , AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException("Invalid token received", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/sign"); // to be executed for all API except /sign
    }

}
