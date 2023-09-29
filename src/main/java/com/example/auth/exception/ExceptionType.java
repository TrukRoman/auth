package com.example.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionType {

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "The request was deemed invalid by an underlying server"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "The requested user not found"),

    EXISTS_USER_WITH_SUCH_EMAIL(HttpStatus.CONFLICT, "The account for the specified email address already exists."),

    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token was expired"),

    PREVIOUS_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "The previous password is not match"),

    INVALID_JWT(HttpStatus.BAD_REQUEST, "Invalid JWT token"),

    EXPIRED_JWT(HttpStatus.BAD_REQUEST, "JWT token is expired"),

    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, "JWT token is unsupported"),

    EMPTY_JWT_CLAIMS(HttpStatus.BAD_REQUEST, "JWT claims string is empty");


    private final HttpStatus status;

    /**
     * Default exception message.
     */
    private final String message;
}
