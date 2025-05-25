package com.reciperestapi.reciperestapi.security.service;

import com.reciperestapi.reciperestapi.common.settings.exceptions.AuthenticationException;
import com.reciperestapi.reciperestapi.security.service.password.PasswordEncoder;
import com.reciperestapi.reciperestapi.user.repository.UserDAO;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.inject.Inject;

public class UserCredentialsValidator {
    @Inject
    UserDAO userDAO;
    @Inject
    PasswordEncoder passwordEncoder;
    public User validateCredentials(String username, String password) {

        var user = userDAO.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid credentials."));
        System.out.println("user: " + user);

        if (!user.isEnable()) {
            // User is not active
            throw new AuthenticationException("Please verify your account.");
        }

        if (!passwordEncoder.checkPassword(password, user.getPassword())) {
            // Invalid password
            throw new AuthenticationException("Invalid credentials.");
        }

        return user;
    }
}
