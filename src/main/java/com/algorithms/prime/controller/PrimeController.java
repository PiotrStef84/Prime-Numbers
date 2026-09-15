package com.algorithms.prime.controller;

import com.algorithms.prime.dto.PrimeResponse;
import com.algorithms.prime.service.PrimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/primes")
@Tag(name = "Prime Numbers", description = "REST API for calculating prime numbers using different sieve algorithms")
public class PrimeController {

    private static final String ALGORITHM_ERATOSTHENES = "eratosthenes";
    private static final String ALGORITHM_LINEAR = "linear";

    private final PrimeService primeService;

    public PrimeController(PrimeService primeService) {
        this.primeService = primeService;
    }

    @Operation(
            summary = "Get prime numbers up to and including N",
            description = "Returns all prime numbers up to and including N. " +
                    "Default algorithm is Sieve of Eratosthenes. " +
                    "Use ?algorithm=linear to switch to Linear Sieve."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully calculated primes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PrimeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input — N is negative, exceeds 5,000,000, or unknown algorithm provided",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized — valid Basic Auth credentials required",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{n}")
    public ResponseEntity<PrimeResponse> getPrimes(
            @Parameter(
                    description = "Upper bound (inclusive). Must be non-negative and not exceed 5,000,000",
                    example = "10"
            )
            @PathVariable int n,
            @Parameter(
                    description = "Algorithm to use. Supported values: eratosthenes (default), linear",
                    example = "eratosthenes"
            )
            @RequestParam(defaultValue = "eratosthenes") String algorithm) {

        var primes = switch (algorithm.toLowerCase()) {
            case ALGORITHM_LINEAR -> primeService.getPrimesLinear(n);
            case ALGORITHM_ERATOSTHENES -> primeService.getPrimesEratosthenes(n);
            default -> throw new IllegalArgumentException(
                    String.format("Unknown algorithm: '%s'. Supported: %s, %s",
                            algorithm, ALGORITHM_ERATOSTHENES, ALGORITHM_LINEAR));
        };

        return ResponseEntity.ok(PrimeResponse.builder()
                .initial(n)
                .primes(primes)
                .build());
    }
}

