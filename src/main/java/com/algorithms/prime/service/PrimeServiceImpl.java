package com.algorithms.prime.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class PrimeServiceImpl implements PrimeService {

    // Safety limit to prevent OutOfMemoryError on large inputs
    private static final int MAX_N = 5_000_000;

    @Override
    public long countPrimesEratosthenes(int n) {
        validateInput(n);

        log.info("Starting Sieve of Eratosthenes calculation for n={}", n);
        long startTime = System.currentTimeMillis();

        if (n <= 2) {
            return 0;
        }

        boolean[] isPrime = new boolean[n];
        // Initialize all entries as true
        for (int i = 2; i < n; i++) {
            isPrime[i] = true;
        }

        // Iterate from 2 to sqrt(n)
        for (int p = 2; p * p < n; p++) {
            if (isPrime[p]) {
                // Mark multiples of p starting from p*p as false
                for (int multiple = p * p; multiple < n; multiple += p) {
                    isPrime[multiple] = false;
                }
            }
        }

        long count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime[i]) {
                count++;
            }
        }

        log.info("Sieve of Eratosthenes completed for n={} in {} ms. Result: {}",
                n, (System.currentTimeMillis() - startTime), count);

        return count;
    }

    @Override
    public long countPrimesLinear(int n) {
        validateInput(n);

        log.info("Starting Linear Sieve calculation for n={}", n);
        long startTime = System.currentTimeMillis();

        if (n <= 2) {
            return 0;
        }

        int[] primes = new int[n];
        int cnt = 0;
        // minPrime[i] stores the smallest prime factor of i
        int[] minPrime = new int[n];

        for (int i = 2; i < n; i++) {
            if (minPrime[i] == 0) {
                primes[cnt++] = i;
                minPrime[i] = i;
            }

            // Iterate through known primes to mark composites
            for (int j = 0; j < cnt; j++) {
                int p = primes[j];

                if ((long) i * p >= n) break;

                minPrime[i * p] = p;

                // If i is divisible by p, then p is the smallest prime factor of i.
                // We stop here to ensure each composite is marked exactly once.
                if (i % p == 0) {
                    break;
                }
            }
        }

        log.info("Linear Sieve completed for n={} in {} ms. Result: {}",
                n, (System.currentTimeMillis() - startTime), cnt);

        return cnt;
    }

    private void validateInput(int n) {
        if (n > MAX_N) {
            throw new IllegalArgumentException(
                    String.format("Input value %d exceeds the maximum allowed limit of %d.", n, MAX_N)
            );
        }
        // Note: Negative or small values are handled by logic returning 0,
        // so no exception is thrown for those.
    }
}

