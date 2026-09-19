package com.algorithms.prime.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PrimeServiceImpl implements PrimeService {

    private static final int MAX_N = 5_000_000;

    @Override
    @Cacheable(value = "primes", key = "#n")
    public List<Integer> getPrimesEratosthenes(int n) {
        validateInput(n);
        if (n < 2) return List.of();

        boolean[] isComposite = new boolean[n + 1];
        for (int p = 2; (long) p * p <= n; p++) {
            if (!isComposite[p]) {
                for (int multiple = p * p; multiple <= n; multiple += p) {
                    isComposite[multiple] = true;
                }
            }
        }

        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (!isComposite[i]) primes.add(i);
        }
        return primes;
    }

    @Override
    @Cacheable(value = "primes", key = "#n + '_linear'")
    public List<Integer> getPrimesLinear(int n) {
        validateInput(n);
        if (n < 2) return List.of();

        int[] minPrime = new int[n + 1];
        List<Integer> primes = new ArrayList<>();

        for (int i = 2; i <= n; i++) {
            if (minPrime[i] == 0) {
                primes.add(i);
                minPrime[i] = i;
            }
            for (int p : primes) {
                if ((long) i * p > n) break;
                minPrime[i * p] = p;
                if (i % p == 0) break;
            }
        }
        return primes;
    }

    private void validateInput(int n) {
        if (n < 0) throw new IllegalArgumentException("Input must be a non-negative integer.");
        if (n > MAX_N) throw new IllegalArgumentException(
                String.format("Input %d exceeds maximum allowed limit of %d.", n, MAX_N));
    }
}
