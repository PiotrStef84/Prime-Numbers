package com.algorithms.prime.service;

import java.util.List;

public interface PrimeService {

    List<Integer> getPrimesEratosthenes(int n);
    List<Integer> getPrimesLinear(int n);
}

