package com.reciperestapi.reciperestapi.security.filters;

import com.reciperestapi.reciperestapi.security.auth.CustomSecurityContext;
import com.reciperestapi.reciperestapi.security.model.Token;
import com.reciperestapi.reciperestapi.security.model.TokenScope;
import com.reciperestapi.reciperestapi.security.repository.impl_service.TokenDAOImpl;
import com.reciperestapi.reciperestapi.security.service.jwt.JwtServiceProvider;
import com.reciperestapi.reciperestapi.common.settings.exceptions.AuthenticationException;
import com.reciperestapi.reciperestapi.common.settings.exceptions.InvalidAuthenticationTokenException;
import com.reciperestapi.reciperestapi.security.service.jwt.JwtServiceParser;
import com.reciperestapi.reciperestapi.user.model.User;
import com.reciperestapi.reciperestapi.user.repository.impl_service.UserDAOImpl;
import com.reciperestapi.reciperestapi.security.auth.AuthenticatedUserDetails;
import jakarta.annotation.Priority;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.http.HttpRequest;
import java.util.*;

@Provider
@Dependent
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private JwtServiceProvider jwtTokenProvider;
    @Inject
    private JwtServiceParser jwtServiceParser;
    @Inject
    private TokenDAOImpl tokenDAO;
    @Inject
    private UserDAOImpl userDAO;
    @Context
    private ResourceInfo resourceInfo;
    private static final Set<String> WHITE_LIST_URLS = new HashSet<>(Arrays.asList(
            "auth/logout",
            "auth/refresh-token"
    ));

    @Override
    public void filter(@NotNull ContainerRequestContext requestContext) {
        final String userName;
        final String jti;
        final TokenScope tokenScope;
        Method method = resourceInfo.getResourceMethod();
        System.out.println(requestContext.getUriInfo().getPath());
        // Check if this is a request to the authentication endpoint itself, skip filter if true
        if (method.isAnnotationPresent(PermitAll.class) || resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class)) {
            return;
        }
        boolean expectRefreshToken = isWhiteListed(requestContext.getUriInfo().getPath());
        String token = extractTokenByType(requestContext, expectRefreshToken);
        userName = jwtServiceParser.parseToken(token).getUserName();
        //jti = jwtServiceParser.parseToken(token).getJti();
        tokenScope = jwtServiceParser.parseToken(token).getTokenScope();
        if (userName != null && requestContext.getSecurityContext().getUserPrincipal() == null) {

            User user = userDAO.findByUsername(userName)
                    .orElseThrow(() -> new AuthenticationException("User does not found."));

            boolean isTokenValid = tokenDAO.findByToken(token)
                    .map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);

           // System.out.println("isTokenValid: " + isTokenValid);

            boolean hasValidRefreshToken = tokenDAO.findAllValidTokenByUserId(user.getUserId())
                    .stream()
                    .anyMatch(t1-> !t1.isRevoked() && !t1.isExpired());

           // System.out.println("hasValidRefreshToken: " + hasValidRefreshToken);

            if (jwtTokenProvider.isTokenValid(token, user.getUsername())){
                if(jwtTokenProvider.isAccessToken(token, TokenScope.valueOf(tokenScope.name().toUpperCase())) && hasValidRefreshToken) {
                    setSecurityContext(requestContext, token, user);
                } else if ((jwtTokenProvider.isRefreshToken(token, TokenScope.valueOf(tokenScope.name().toUpperCase()))
                        && isTokenValid) && isWhiteListed(requestContext.getUriInfo().getPath())) {
                    setSecurityContext(requestContext, token, user);
                }else {
                    throw new InvalidAuthenticationTokenException("Invalid token scope.");
                }

            } else {
                throw new InvalidAuthenticationTokenException("filter: Invalid or expired token.");
            }

        } else {
            throw new InvalidAuthenticationTokenException("Invalid token or security context already set.");
        }

    }

    private String extractTokenByType(ContainerRequestContext requestContext, boolean expectRefreshToken) {
        try {
            return  extractTokenBearer(requestContext);
        } catch (InvalidAuthenticationTokenException e){
            String token = extractTokenCookie(requestContext, expectRefreshToken);
            if (token == null) {
                throw new InvalidAuthenticationTokenException("Invalid or expired token.");
            }
           return token;
        }
    }

   private String extractTokenBearer(ContainerRequestContext requestContext) throws InvalidAuthenticationTokenException {
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidAuthenticationTokenException("Missing or invalid Authorization header.");
        }
        return authHeader.substring(7);
    }
    private String extractTokenCookie(ContainerRequestContext requestContext, boolean expectRefreshToken) throws InvalidAuthenticationTokenException {
        Map<String, Cookie> cookies = requestContext.getCookies();
        String tokenCookieName = expectRefreshToken ? "refresh_token" : "access_token";
        System.out.println("cookie -> " + tokenCookieName);
        Cookie tokenCookie = cookies.get(tokenCookieName);
        if (tokenCookie == null || tokenCookie.getValue().isEmpty()) {
            throw new InvalidAuthenticationTokenException("Missing or invalid " + tokenCookieName + " cookie.");
        }
        return tokenCookie.getValue();
    }
    private void setSecurityContext(ContainerRequestContext requestContext, String token, User user) {
        Token authenticationTokenDetails = jwtTokenProvider.parseToken(token);
        //System.out.println("authenticationTokenDetails: " + authenticationTokenDetails);
        AuthenticatedUserDetails authenticatedUserDetails = new AuthenticatedUserDetails(user, user.getRole());
        boolean isSecure = requestContext.getSecurityContext().isSecure();
        //System.out.println("isSecure: " + isSecure);
        SecurityContext securityContext = new CustomSecurityContext(authenticatedUserDetails, authenticationTokenDetails, isSecure);
        requestContext.setSecurityContext(securityContext);
    }

    private boolean isWhiteListed(String path) {
        return WHITE_LIST_URLS.stream().anyMatch(path::startsWith);
    }

}
