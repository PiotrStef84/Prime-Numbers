package com.algorithms.prime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrimeCountResponse {
    private String algorithm;
    private int inputN;
    private long countPrimes;
}

