package com.algorithms.prime.controller;


import com.algorithms.prime.dto.PrimeCountResponse;
import com.algorithms.prime.service.PrimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/primes")
public class PrimeController {

    private final PrimeService primeService;

    public PrimeController(PrimeService primeService) {
        this.primeService = primeService;
    }

    @GetMapping("/eratosthenes")
    public ResponseEntity<PrimeCountResponse> countWithEratosthenes(@RequestParam int n) {
        long result = primeService.countPrimesEratosthenes(n);

        return ResponseEntity.ok(
                PrimeCountResponse.builder()
                        .algorithm("Sieve of Eratosthenes")
                        .inputN(n)
                        .countPrimes(result)
                        .build()
        );
    }

    @GetMapping("/linear")
    public ResponseEntity<PrimeCountResponse> countWithLinear(@RequestParam int n) {
        long result = primeService.countPrimesLinear(n);

        return ResponseEntity.ok(
                PrimeCountResponse.builder()
                        .algorithm("Linear Sieve (Euler's)")
                        .inputN(n)
                        .countPrimes(result)
                        .build()
        );
    }
}
