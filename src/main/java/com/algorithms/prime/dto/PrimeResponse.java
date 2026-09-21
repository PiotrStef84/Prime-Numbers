package com.algorithms.prime.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "PrimeResponse")
@Schema(description = "Prime number calculation response")
public class PrimeResponse {

    @Schema(description = "The input number N provided by the caller", example = "10")
    @JsonProperty("Initial")
    private int initial;

    @Schema(description = "List of all prime numbers up to and including N", example = "[2, 3, 5, 7]")
    @JsonProperty("Primes")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Primes")
    private List<Integer> primes;
}

