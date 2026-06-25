package com.gymmaster.exception;

/**
 * Application-layer exception for business rule violations.
 * Controllers should throw this instead of generic {@link RuntimeException}
 * when a user-visible constraint is breached (e.g. venue already fully booked,
 * duplicate registration, insufficient balance, etc.).
 *
 * <p>The {@link com.gymmaster.common.GlobalExceptionHandler} maps this to HTTP 422.</p>
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
