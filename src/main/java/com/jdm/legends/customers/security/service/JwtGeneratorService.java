package com.jdm.legends.customers.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

import static java.util.stream.Collectors.joining;

@Service
public class JwtGeneratorService {
    private final long jwtExpirationTime; // 8h
    private final String jwtSecretKey;

    public JwtGeneratorService(@Value("${jwt.auth.expires_in}") long jwtExpirationTime, @Value("${jwt.auth.secret_key}") String jwtSecretKey) {
        this.jwtExpirationTime = jwtExpirationTime;
        this.jwtSecretKey = jwtSecretKey;
    }

    public final String generateJwtToken(Authentication authentication, SecretKey secretKey) {
        return Jwts.builder()
                .setIssuer("JDM-Legends-Customers")
                .setSubject("JWT Token")
                .claim("username", authentication.getName())
                .claim("authorization", computeAuthorities(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationTime))
                .signWith(secretKey)
                .compact();
    }

    public final SecretKey computeSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    private String computeAuthorities(Collection<? extends GrantedAuthority> collection) {
        return collection.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(joining(","));
    }
}
