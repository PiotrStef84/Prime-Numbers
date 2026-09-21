
package com.algorithms.prime.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;



import static org.assertj.core.api.Assertions.*;

class PrimeServiceImplTest {

    private PrimeServiceImpl primeService;

    @BeforeEach
    void setUp() {
        primeService = new PrimeServiceImpl();
    }

    // --- Eratosthenes ---

    @Test
    void eratosthenes_shouldReturnCorrectPrimes_forN10() {
        assertThat(primeService.getPrimesEratosthenes(10))
                .containsExactly(2, 3, 5, 7);
    }

    @Test
    void eratosthenes_shouldIncludeN_whenNIsPrime() {
        assertThat(primeService.getPrimesEratosthenes(7))
                .containsExactly(2, 3, 5, 7);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void eratosthenes_shouldReturnEmpty_whenNLessThan2(int n) {
        assertThat(primeService.getPrimesEratosthenes(n)).isEmpty();
    }

    @Test
    void eratosthenes_shouldReturnEmpty_forN2() {
        assertThat(primeService.getPrimesEratosthenes(2))
                .containsExactly(2);
    }

    @Test
    void eratosthenes_shouldThrow_whenNExceedsMaxLimit() {
        assertThatThrownBy(() -> primeService.getPrimesEratosthenes(5_000_001))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exceeds maximum allowed limit");
    }

    @Test
    void eratosthenes_shouldThrow_whenNIsNegative() {
        assertThatThrownBy(() -> primeService.getPrimesEratosthenes(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // --- Linear Sieve ---

    @Test
    void linear_shouldReturnCorrectPrimes_forN10() {
        assertThat(primeService.getPrimesLinear(10))
                .containsExactly(2, 3, 5, 7);
    }

    @Test
    void linear_shouldIncludeN_whenNIsPrime() {
        assertThat(primeService.getPrimesLinear(7))
                .containsExactly(2, 3, 5, 7);
    }

    @Test
    void linear_shouldReturnEmpty_forN2() {
        assertThat(primeService.getPrimesLinear(2))
                .containsExactly(2);
    }

    @Test
    void linear_shouldThrow_whenNIsNegative() {
        assertThatThrownBy(() -> primeService.getPrimesLinear(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void linear_shouldReturnEmpty_whenNLessThan2(int n) {
        assertThat(primeService.getPrimesLinear(n)).isEmpty();
    }

    @Test
    void linear_shouldThrow_whenNExceedsMaxLimit() {
        assertThatThrownBy(() -> primeService.getPrimesLinear(5_000_001))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exceeds maximum allowed limit");
    }

    // --- Both algorithms produce same results ---

    @Test
    void bothAlgorithms_shouldReturnSameResults_forN100() {
        assertThat(primeService.getPrimesEratosthenes(100))
                .isEqualTo(primeService.getPrimesLinear(100));
    }
}

