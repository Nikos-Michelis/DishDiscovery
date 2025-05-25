package com.reciperestapi.reciperestapi.security.service;

import com.reciperestapi.reciperestapi.security.auth.CustomSecurityContext;
import com.reciperestapi.reciperestapi.security.model.Token;
import com.reciperestapi.reciperestapi.security.repository.impl_service.TokenDAOImpl;
import com.reciperestapi.reciperestapi.common.settings.exceptions.InvalidAuthenticationTokenException;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

@RequiredArgsConstructor
public class LogoutService {

    @Inject
    private TokenDAOImpl tokenDAO;

    public void logoutUser(SecurityContext securityContext) {
        Token token = ((CustomSecurityContext) securityContext).getToken();
        if (securityContext.getUserPrincipal() != null) {
            var storedToken = tokenDAO.findByToken(token.getToken())
                    .orElseThrow(() -> new InvalidAuthenticationTokenException("Invalid or non-existent token."));
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenDAO.update(Collections.singletonList(storedToken));
            ((CustomSecurityContext) securityContext).clear();
        } else {
            throw new InvalidAuthenticationTokenException("Missing or invalid Authorization header.");
        }
    }
}
