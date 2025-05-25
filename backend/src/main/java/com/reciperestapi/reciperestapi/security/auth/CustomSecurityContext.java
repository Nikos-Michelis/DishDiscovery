package com.reciperestapi.reciperestapi.security.auth;

import com.reciperestapi.reciperestapi.security.model.Roles;
import com.reciperestapi.reciperestapi.security.model.Token;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;
@Data
@AllArgsConstructor
public class CustomSecurityContext implements SecurityContext {

    private AuthenticatedUserDetails authenticatedUserDetails;
    private Token token;
    private final boolean isSecure;


    @Override
    public Principal getUserPrincipal() {
        return authenticatedUserDetails;
    }

    @Override
    public boolean isUserInRole(String role) {
        return authenticatedUserDetails.getRoles().contains(Roles.valueOf(role));
    }
    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
    public void clear() {
        // Clear user details and other security context information
        this.authenticatedUserDetails = null;
        this.token = null;
    }
}
