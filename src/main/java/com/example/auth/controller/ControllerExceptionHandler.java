package com.example.auth.controller;

import com.example.auth.dto.error.ErrorResponse;
import com.example.auth.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String UNEXPECTED_EXCEPTION = "unexpected_exception";
    private static final String CONSTRAINT_FAILED = "constraint_failed";
    private static final String CONSTRAINT_FAILED_DESC = "Validation on one or more fields has failed. See more in details.";
    private static final String ACCESS_DENIED = "access_denied";
    private static final String ACCESS_DENIED_DESC = "Insufficient authority.";
    private static final String NO_PERMISSIONS = "No permission to get this resource";
    private static final String AUTHENTICATION_ERROR = "authentication_error";

    private static final String BAD_CREDENTIALS = "bad_credentials";
    private static final String BAD_CREDENTIALS_DESC = "Invalid username or password.";

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        log.debug(exception.getMessage(), exception);

        Map<String, List<String>> fieldErrors = new HashMap<>();
        for (var fieldError : exception.getBindingResult().getFieldErrors()) {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();

            fieldErrors.computeIfAbsent(fieldName, key -> new ArrayList<>()).add(errorMessage);
        }

        var response = ErrorResponse.builder()
                .error(CONSTRAINT_FAILED)
                .description(CONSTRAINT_FAILED_DESC)
                .details(fieldErrors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<ErrorResponse> handleServiceExceptions(ServiceException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(exception.getType().name())
                .description(exception.getDescription())
                .build();

        return new ResponseEntity<>(errorResponse, exception.getType().getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception) {
        var response = ErrorResponse.builder()
                .error(ACCESS_DENIED)
                .description(ACCESS_DENIED_DESC)
                .build();

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {

        var response = ErrorResponse.builder()
                .error(ACCESS_DENIED)
                .description(NO_PERMISSIONS)
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(BAD_CREDENTIALS)
                .description(BAD_CREDENTIALS_DESC)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleAny(Exception exception, WebRequest request) {
        log.error("Unexpected error occurred during request {}", request.toString(), exception);
        var response = ErrorResponse.builder()
                .error(UNEXPECTED_EXCEPTION)
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
