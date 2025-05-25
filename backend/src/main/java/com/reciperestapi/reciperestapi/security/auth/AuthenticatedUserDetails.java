package com.reciperestapi.reciperestapi.security.auth;

import com.reciperestapi.reciperestapi.security.model.Roles;
import com.reciperestapi.reciperestapi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;
import java.util.Set;

@Data
@AllArgsConstructor
public final class AuthenticatedUserDetails implements Principal {

    private final User user;
    private final Set<Roles> roles;
    @Override
    public String getName() {
        return user.getUsername();
    }
}