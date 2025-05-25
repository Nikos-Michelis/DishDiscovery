package com.reciperestapi.reciperestapi.common.settings.handler;

import com.reciperestapi.reciperestapi.common.settings.exceptions.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<RuntimeException> {

    private Map<String, Object> httpResponse(String message, Exception ex, Response.Status status) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", message);
        map.put("status", status);
        return map;
    }

    @Override
    public Response toResponse(RuntimeException ex) {
        System.out.println("Exception type: " + ex.getClass() + " ---> " + ex.getMessage());
        ex.getStackTrace();

        if (ex instanceof NotFoundException) {
            return notFoundException((NotFoundException) ex);
        } else if (ex instanceof ClientErrorException) {
            return handleClientErrorException((ClientErrorException) ex);
        } else if (ex instanceof ServerErrorException) {
            return handleServerErrorException((ServerErrorException) ex);
        } else if (ex instanceof AccessDeniedException) {
            return handleAccessDeniedException((AccessDeniedException) ex);
        } else if (ex instanceof ResourceNotFoundException) {
            return handleNotFoundException((ResourceNotFoundException) ex);
        } else if (ex instanceof AuthenticationException) {
            return handleAuthenticationException((AuthenticationException) ex);
        } else if (ex instanceof ActivationTokenException) {
            return handleActivationTokenException((ActivationTokenException) ex);
        } else if (ex instanceof ResendOtpLimitException) {
            return handleResendOtpLimitException((ResendOtpLimitException) ex);
        } else if (ex instanceof UsernameNotFoundException){
            return handleUsernameNotFoundException((UsernameNotFoundException) ex);
        } else if (ex instanceof LockedException){
            return handleLockedException((LockedException) ex);
        }
        else {
            return handleGenericException(ex);
        }
    }
    private Response handleGenericException(Exception ex) {
        // Handle other types of exceptions or provide a generic response
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        Map<String, Object> map = httpResponse(ex.getMessage(), ex, status);
        ex.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }

    private Response handleClientErrorException(ClientErrorException ex){
        System.out.println(ex.getMessage());
        Map<String, Object> map = httpResponse(ex.getMessage(), ex,Response.Status.fromStatusCode(ex.getResponse().getStatus()));
        return Response.status(ex.getResponse().getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
    private Response handleServerErrorException(ServerErrorException ex){
        System.out.println(ex.getMessage());
        Map<String, Object> map = httpResponse(ex.getMessage(), ex,Response.Status.fromStatusCode(ex.getResponse().getStatus()));
        return Response.status(ex.getResponse().getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
    private Response handleNotAllowedException(NotAllowedException ex){
        Response.Status status = Response.Status.METHOD_NOT_ALLOWED;
        System.out.println(ex.getMessage());
        Map<String, Object> map = httpResponse(ex.getMessage(), ex, status);
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
    private Response handleNotFoundException(ResourceNotFoundException ex) {
        Response.Status status = Response.Status.NOT_FOUND;
        Map<String, Object> map = httpResponse(ex.getLocalizedMessage(), ex, status);
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
    private Response notFoundException(NotFoundException ex) {
        // Handle RuntimeException
        Response.Status status = Response.Status.NOT_FOUND;
        Map<String, Object> map = httpResponse(ex.getMessage(), ex, status);
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
    private Response handleAccessDeniedException(AccessDeniedException ex){
        Response.Status status = Response.Status.FORBIDDEN;
        Map<String, Object> map = httpResponse(ex.getMessage(), ex, status);
        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
    private Response handleAuthenticationException(AuthenticationException ex) {
        Response.Status status = Response.Status.UNAUTHORIZED;
        Map<String, Object> map = httpResponse(ex.getMessage(), ex, status);
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
    private Response handleActivationTokenException(ActivationTokenException ex) {
        Response.Status status = Response.Status.UNAUTHORIZED;
        Map<String, Object> map = httpResponse(ex.getMessage(), ex, status);
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
    private Response handleResendOtpLimitException(ResendOtpLimitException ex) {
            Response.Status status = Response.Status.TOO_MANY_REQUESTS;
            Map<String, Object> map = httpResponse(ex.getMessage(), ex, status);
            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(map)
                    .build();
    }
    private Response handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Response.Status status = Response.Status.NOT_FOUND;
        Map<String, Object> map = httpResponse(ex.getLocalizedMessage(), ex, status);
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(map)
                .build();
    }
      private Response handleLockedException(LockedException ex) {
            Response.Status status = Response.Status.UNAUTHORIZED;
            Map<String, Object> map = httpResponse(ex.getLocalizedMessage(), ex, status);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(map)
                    .build();
        }

}
