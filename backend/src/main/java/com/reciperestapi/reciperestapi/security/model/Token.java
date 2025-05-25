package com.reciperestapi.reciperestapi.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    private String jti;
    private String token;
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
    private ZonedDateTime expiresAt;
    private ZonedDateTime issuedAt;
    private String ipAddress;
    private TokenScope tokenScope;
    private Integer userId;
    private Set<Roles> roles;
    private String userName;

}
