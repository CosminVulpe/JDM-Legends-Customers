package com.jdm.legends.customers.integration.service;

import com.jdm.legends.customers.security.service.JwtGeneratorService;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import static com.jdm.legends.customers.utils.TestDummy.getCustomMock;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test-in-memory")
@Transactional
public class JwtGeneratorServiceIT {
    private static final String KEY_TOKEN = "vbabbvaybyudbvyvaysubysudvbfayfgaysdgfyabvufgvauscvasuvcusufausdgfyuas";

    @Autowired
    private JwtGeneratorService jwtGeneratorService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void init() {
        jwtGeneratorService = new JwtGeneratorService(30000000, KEY_TOKEN);
    }

    @Test
    void generateJwtToken() {
        Customer customMock = getCustomMock();
        customMock.setPwd(passwordEncoder.encode(customMock.getPwd()));
        customerRepository.saveAndFlush(customMock);
        TestingAuthenticationToken token = new TestingAuthenticationToken(getCustomMock().getEmailAddress()
                , getCustomMock().getPwd());
        UsernamePasswordAuthenticationToken authenticatedToken = (UsernamePasswordAuthenticationToken)
                authenticationProvider.authenticate(token);

        SecretKey secretKey = Keys.hmacShaKeyFor(KEY_TOKEN.getBytes(StandardCharsets.UTF_8));
        String jwtToken = jwtGeneratorService.generateJwtToken(authenticatedToken, secretKey);

        assertThat(jwtToken).isNotEmpty();
    }

    @Test
    void computeSecretKey() {
        SecretKey secretKey = jwtGeneratorService.computeSecretKey();
        assertThat(secretKey).isNotNull();
    }
}
