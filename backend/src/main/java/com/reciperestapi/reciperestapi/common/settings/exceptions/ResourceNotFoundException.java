package com.reciperestapi.reciperestapi.common.settings.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class ResourceNotFoundException extends WebApplicationException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
