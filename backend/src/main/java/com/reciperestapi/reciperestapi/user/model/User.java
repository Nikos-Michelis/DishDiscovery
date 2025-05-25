package com.reciperestapi.reciperestapi.user.model;

import com.reciperestapi.reciperestapi.security.model.Roles;
import com.reciperestapi.reciperestapi.security.model.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer userId;
    private String userUUId;
    private String username;
    private String email;
    private String password;
    private boolean enable;
    private boolean accountLocked;
    private LocalDateTime lockedAt;
    private LocalDateTime lockExpiresAt;
    private int totalBlocks;
    private int totalAttempts;
    private Set<Integer> roleId;
    private Set<Roles> role;
    private List<Token> tokens;
}
