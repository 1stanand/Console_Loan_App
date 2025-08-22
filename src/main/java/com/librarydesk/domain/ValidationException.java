package com.librarydesk.domain;

/** Custom validation exception for domain errors. */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
