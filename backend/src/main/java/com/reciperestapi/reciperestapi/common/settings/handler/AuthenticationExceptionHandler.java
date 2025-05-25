package com.reciperestapi.reciperestapi.common.settings.handler;

import com.reciperestapi.reciperestapi.common.settings.exceptions.InvalidAuthenticationTokenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class AuthenticationExceptionHandler implements ExceptionMapper<InvalidAuthenticationTokenException> {

    private Map<String, Object> httpResponse(String message, Exception ex, Response.Status status) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", message);
        map.put("status", status);
        return map;
    }
    @Override
    public Response toResponse(InvalidAuthenticationTokenException ex) {
        System.out.println("Exception type: " + ex.getClass() + " ---> " + ex.getMessage());
        Response.Status status = Response.Status.UNAUTHORIZED;
        Map<String, Object> map = httpResponse(ex.getMessage(), ex, status);
        return Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
}
