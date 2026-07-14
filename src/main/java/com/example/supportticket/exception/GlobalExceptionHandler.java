package com.example.supportticket.exception;

import com.example.supportticket.common.ErrorResponse;
import com.example.supportticket.common.ErrorResponse.FieldViolation;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFound(
            TicketNotFoundException exception,
            HttpServletRequest request
    ) {
        log.warn("Ticket lookup failed for {}: {}", request.getRequestURI(), exception.getMessage());
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<FieldViolation> violations = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldViolation(error.getField(), error.getDefaultMessage()))
                .toList();

        log.warn("Request validation failed for {}: {}", request.getRequestURI(), violations);
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                "Request validation failed",
                request.getRequestURI(),
                violations
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableRequest(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        log.warn("Malformed request body for {}", request.getRequestURI());
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                "Malformed request body",
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }
}
