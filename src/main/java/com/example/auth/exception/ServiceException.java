package com.example.auth.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final ExceptionType type;
    private final String description;

    public ServiceException(ExceptionType type, String message) {
        super(type.name() + ": " + message);
        this.type = type;
        this.description = message;
    }

    public ServiceException(ExceptionType type) {
        super(type.name() + ": " + type.getMessage());
        this.type = type;
        this.description = type.getMessage();
    }
}