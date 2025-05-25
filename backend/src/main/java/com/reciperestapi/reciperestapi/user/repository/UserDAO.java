package com.reciperestapi.reciperestapi.user.repository;

import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.model.Roles;
import com.reciperestapi.reciperestapi.user.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserDAO {
    Optional<User> findByUsername(String username);
    Optional<User> findUserById(User user);
    Optional<User> findUserByEmail(String email);
    Optional<User> save(User user);
    Optional<User> update(User user);
    void updateStatus(User user);
    Set<Roles> findRoleByUserName(String userName);
    void saveRole(int userId);
    void updateAttempts(User user);
    void updateLock(User user);
}