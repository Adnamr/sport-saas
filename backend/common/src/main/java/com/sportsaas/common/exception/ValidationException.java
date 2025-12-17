package com.sportsaas.common.exception;

/**
 * Exception levée lors d'une erreur de validation métier.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
