package com.algorithms.prime.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Error response returned on bad requests or server errors")
public class ErrorResponse {

    @Schema(description = "Timestamp of when the error occurred", example = "2026-09-15T10:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP error description", example = "Bad Request")
    private String error;

    @Schema(description = "Detailed error message", example = "Unknown algorithm: 'foo'. Supported: eratosthenes, linear")
    private String message;
}

