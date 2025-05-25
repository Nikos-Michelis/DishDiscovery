package com.reciperestapi.reciperestapi.common.settings.exceptions;

public class InvalidAuthenticationTokenException extends RuntimeException {
    public InvalidAuthenticationTokenException(String message) {
        super(message);
    }
    public InvalidAuthenticationTokenException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
