package com.gymmaster.common;

import com.gymmaster.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler — translates exceptions into a uniform
 * {@link BackMsg} response so that raw stack traces are never exposed to clients.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Handles @Valid / @Validated constraint violations on request bodies. */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BackMsg<Void> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.debug("Validation failed: {}", message);
        return BackMsg.error(HttpStatus.BAD_REQUEST.value(), message);
    }

    /** Handles missing required request parameters. */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BackMsg<Void> handleMissingParam(MissingServletRequestParameterException ex) {
        log.debug("Missing parameter: {}", ex.getParameterName());
        return BackMsg.error(HttpStatus.BAD_REQUEST.value(),
                "Required parameter missing: " + ex.getParameterName());
    }

    /** Handles application-layer business rule violations. */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BusinessException.class)
    public BackMsg<Void> handleBusiness(BusinessException ex) {
        log.warn("Business exception: {}", ex.getMessage());
        return BackMsg.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
    }

    /** JWT token has expired — return 401 instead of 500. */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public BackMsg<Void> handleExpiredJwt(ExpiredJwtException ex) {
        log.debug("JWT expired: {}", ex.getMessage());
        return BackMsg.error(HttpStatus.UNAUTHORIZED.value(), "Token has expired, please login again.");
    }

    /** JWT token is malformed or has a bad signature — return 401. */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({MalformedJwtException.class, SignatureException.class})
    public BackMsg<Void> handleBadJwt(Exception ex) {
        log.warn("Invalid JWT: {}", ex.getMessage());
        return BackMsg.error(HttpStatus.UNAUTHORIZED.value(), "Invalid or tampered token.");
    }

    /** Spring Security access denial (should normally be handled by AccessDeniedHandler,
     *  but guard here for completeness). */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public BackMsg<Void> handleAccessDenied(AccessDeniedException ex) {
        return BackMsg.error(HttpStatus.FORBIDDEN.value(), "Access denied.");
    }

    /** Catch-all — log the full stack trace server-side, return generic 500 to client. */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BackMsg<Void> handleGeneral(Exception ex) {
        log.error("Unhandled exception", ex);
        return BackMsg.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later.");
    }
}
