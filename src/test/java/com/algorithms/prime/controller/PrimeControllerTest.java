package com.algorithms.prime.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@SpringBootTest
@AutoConfigureMockMvc
class PrimeControllerTest {

    private static final String TEST_USER = "user";
    private static final String TEST_PASSWORD = "password";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // --- Security ---

    @Test
    void getPrimes_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/primes/10"))
                .andExpect(status().isUnauthorized());
    }

    // --- Default algorithm ---

    @Test
    void getPrimes_shouldReturnCorrectJson_withDefaultAlgorithm() throws Exception {
        mockMvc.perform(get("/api/v1/primes/10")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Initial").value(10))
                .andExpect(jsonPath("$.Primes").isArray())
                .andExpect(jsonPath("$.Primes[0]").value(2))
                .andExpect(jsonPath("$.Primes[1]").value(3))
                .andExpect(jsonPath("$.Primes[2]").value(5))
                .andExpect(jsonPath("$.Primes[3]").value(7));
    }

    // --- Explicit algorithm selection ---

    @Test
    void getPrimes_shouldReturnCorrectJson_withEratosthenesParam() throws Exception {
        mockMvc.perform(get("/api/v1/primes/10")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD))
                        .param("algorithm", "eratosthenes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Initial").value(10))
                .andExpect(jsonPath("$.Primes[0]").value(2));
    }

    @Test
    void getPrimes_shouldReturnCorrectJson_withLinearParam() throws Exception {
        mockMvc.perform(get("/api/v1/primes/10")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD))
                        .param("algorithm", "linear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Initial").value(10))
                .andExpect(jsonPath("$.Primes[0]").value(2));
    }

    // --- Case insensitivity ---

    @Test
    void getPrimes_shouldBeCaseInsensitive_forAlgorithmParam() throws Exception {
        mockMvc.perform(get("/api/v1/primes/10")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD))
                        .param("algorithm", "LINEAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Primes[0]").value(2));
    }

    // --- Edge cases ---

    @Test
    void getPrimes_shouldReturnEmptyPrimes_forN1() throws Exception {
        mockMvc.perform(get("/api/v1/primes/1")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Primes").isEmpty());
    }

    @Test
    void getPrimes_shouldReturnSinglePrime_forN2() throws Exception {
        mockMvc.perform(get("/api/v1/primes/2")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Primes[0]").value(2));
    }

    // --- Error cases ---

    @Test
    void getPrimes_shouldReturn400_forUnknownAlgorithm() throws Exception {
        mockMvc.perform(get("/api/v1/primes/10")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD))
                        .param("algorithm", "unknown"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Unknown algorithm: 'unknown'. Supported: eratosthenes, linear"));
    }

    @Test
    void getPrimes_shouldReturn400_whenNExceedsLimit() throws Exception {
        mockMvc.perform(get("/api/v1/primes/5000001")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("exceeds maximum allowed limit")));
    }

    @Test
    void getPrimes_shouldReturn400_forNegativeN() throws Exception {
        mockMvc.perform(get("/api/v1/primes/-1")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    // XML content negotiation
    @Test
    void getPrimes_shouldReturnXml_whenAcceptHeaderIsXml() throws Exception {
        mockMvc.perform(get("/api/v1/primes/10")
                        .with(httpBasic(TEST_USER, TEST_PASSWORD))
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(xpath("/PrimeResponse/Initial").string("10"))
                .andExpect(xpath("/PrimeResponse/Primes[1]").string("2"))
                .andExpect(xpath("/PrimeResponse/Primes[2]").string("3"))
                .andExpect(xpath("/PrimeResponse/Primes[3]").string("5"))
                .andExpect(xpath("/PrimeResponse/Primes[4]").string("7"));
    }
}

