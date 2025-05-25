package com.reciperestapi.reciperestapi.common.settings.exceptions;

public class LockedException extends RuntimeException {
    public LockedException(String msg) {
        super(msg);
    }

    public LockedException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
