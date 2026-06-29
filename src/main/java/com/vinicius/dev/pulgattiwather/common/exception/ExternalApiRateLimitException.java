package com.vinicius.dev.pulgattiwather.common.exception;

public class ExternalApiRateLimitException extends ExternalApiException {

    public ExternalApiRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
