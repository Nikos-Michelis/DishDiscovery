package com.reciperestapi.reciperestapi.common.settings.handler;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.*;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    private Map<String, Object> httpResponseErrorList(List<String> message, Response.Status status) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", message);
        map.put("status", status);
        return map;
    }
    @Override
    public Response toResponse(ConstraintViolationException ex) {
        System.out.println("Exception type: " + ex.getClass() + " ---> " + ex.getMessage());
        Response.Status status = Response.Status.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        for (var violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }
        Collections.sort(errors);
        Map<String, Object> map = httpResponseErrorList(errors, status);

        // Log the errors
        System.out.println("Validation errors: " + errors);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
}
