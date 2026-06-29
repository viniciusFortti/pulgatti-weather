package com.vinicius.dev.pulgattiwather.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ProblemDetail handleDuplicateEmail(DuplicateEmailException ex) {
        return problem(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
    public ProblemDetail handleInvalidCredentials(Exception ex) {
        return problem(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    @ExceptionHandler(ExternalApiRateLimitException.class)
    public ProblemDetail handleExternalApiRateLimit(ExternalApiRateLimitException ex) {
        return problem(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(ExternalApiException.class)
    public ProblemDetail handleExternalApi(ExternalApiException ex) {
        return problem(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return problem(HttpStatus.BAD_REQUEST, message);
    }

    private ProblemDetail problem(HttpStatus status, String message) {
        return ProblemDetail.forStatusAndDetail(status, message);
    }
}
