package com.jdm.legends.customers.unit;

import com.jdm.legends.customers.security.service.JwtGeneratorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;

@ExtendWith(MockitoExtension.class)
public class JwtGeneratorServiceUnitTest {
    private JwtGeneratorService jwtGeneratorService;

    @BeforeEach
    void init() {
        jwtGeneratorService = new JwtGeneratorService(30000000
                , "vbabbvaybyudbvyvaysubysudvbfayfgaysdgfyabvufgvauscvasuvcusufausdgfyuas");
    }

    @Test
    void computeSecretKeySuccessfully() {
        SecretKey secretKey = jwtGeneratorService.computeSecretKey();
        Assertions.assertThat(secretKey).isNotNull();
    }
}
