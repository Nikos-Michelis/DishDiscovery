package com.reciperestapi.reciperestapi.user.services;

import com.reciperestapi.reciperestapi.security.auth.AuthenticatedUserDetails;
import com.reciperestapi.reciperestapi.security.auth.CustomSecurityContext;
import com.reciperestapi.reciperestapi.security.service.AuthenticationService;
import com.reciperestapi.reciperestapi.security.service.password.PasswordEncoder;
import com.reciperestapi.reciperestapi.common.settings.exceptions.AuthenticationException;
import com.reciperestapi.reciperestapi.user.dto.ChangePasswordRequest;
import com.reciperestapi.reciperestapi.user.model.User;
import com.reciperestapi.reciperestapi.user.repository.impl_service.UserDAOImpl;
import jakarta.inject.Inject;

import java.util.logging.Logger;

public class UserService {
    @Inject
    private PasswordEncoder passwordEncoder;
    @Inject
    private UserDAOImpl userDAO;
    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());

    public void changePassword(ChangePasswordRequest request, CustomSecurityContext connectedUser) {
        LOGGER.info("connectedUser: " + connectedUser);
        User user = ((AuthenticatedUserDetails) connectedUser.getUserPrincipal()).getUser();
        LOGGER.info("user: " + user);
        if(!passwordEncoder.checkPassword(request.getCurrentPassword(), user.getPassword())){
            throw new AuthenticationException("Wrong Password.");
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new AuthenticationException("Password does not matches.");
        }
        user.setPassword(passwordEncoder.hashPassword(request.getNewPassword()));
        userDAO.update(user);
    }
}
