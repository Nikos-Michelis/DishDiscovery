package com.reciperestapi.reciperestapi.security.service.password;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordEncoder {
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    public boolean checkPassword(String plainTextPassword, String hashedPassword) {

        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new RuntimeException("Hashed password is invalid");
        }
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
