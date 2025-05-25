package com.reciperestapi.reciperestapi.common.settings.exceptions;

public class ResendOtpLimitException extends RuntimeException {
    public ResendOtpLimitException(String message) {
        super(message);
    }
}
