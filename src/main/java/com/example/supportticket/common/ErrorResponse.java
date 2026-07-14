package com.example.supportticket.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        @Schema(example = "404") int status,
        @Schema(example = "Not Found") String error,
        String message,
        String path,
        List<FieldViolation> violations
) {

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        return of(status, message, path, List.of());
    }

    public static ErrorResponse of(
            HttpStatus status,
            String message,
            String path,
            List<FieldViolation> violations
    ) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                violations
        );
    }

    public record FieldViolation(String field, String message) {
    }
}
