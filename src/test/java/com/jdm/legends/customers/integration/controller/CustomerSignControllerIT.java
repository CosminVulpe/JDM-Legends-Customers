package com.jdm.legends.customers.integration.controller;

import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.util.Base64;

import static com.jdm.legends.customers.utils.TestDummy.getCustomMock;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test-in-memory")
@Transactional
public class CustomerSignControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository repository;

    @BeforeEach()
    void init() {
        Customer customMock = getCustomMock();
        customMock.setPwd(passwordEncoder.encode(customMock.getPwd()));
        repository.saveAndFlush(customMock);
    }

    @Test
    void signInSuccessfully() throws Exception {
        // Create an authentication token with user credentials
        TestingAuthenticationToken token = new TestingAuthenticationToken(getCustomMock().getEmailAddress()
                , getCustomMock().getPwd());

        // Authenticate the user using the authentication provider
        UsernamePasswordAuthenticationToken authenticatedToken = (UsernamePasswordAuthenticationToken) authenticationProvider.authenticate(token);

        assertThat(authenticatedToken).isNotNull();
        assertThat(authenticatedToken.getPrincipal()).isNotNull();
        assertThat(authenticatedToken.getCredentials()).isNotNull();
        assertThat(authenticatedToken.getAuthorities()).isNotEmpty();

        String userName = authenticatedToken.getPrincipal().toString();
        String pwd = authenticatedToken.getCredentials().toString();

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.get("/sign")
                        .header(AUTHORIZATION, computeBasicAuthToken(userName, pwd)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertThat(contentAsString).isNotEmpty();
        assertThat(contentAsString).isEqualTo(getCustomMock().getUserName());
    }

    private String computeBasicAuthToken(String userName, String pwd) {
        String token = userName.concat(":").concat(pwd);
        return "Basic ".concat(
                new String(
                        Base64.getEncoder().encode(token.getBytes(UTF_8))
                )
        );

    }
}
