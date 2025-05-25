package com.reciperestapi.reciperestapi.security.dto;

import com.reciperestapi.reciperestapi.security.model.Roles;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonbPropertyOrder({"status", "message", "email"})
public class ResponseTokenDetails {
    private String jti;
    private String accessToken;
    private String refreshToken;
    private String userName;
    private String email;
    private Set<Roles> roles;
    private ZonedDateTime accessTokenIssuedAt;
    private ZonedDateTime accessTokenExpiresAt;
    private ZonedDateTime refreshTokenIssuedAt;
    private ZonedDateTime refreshTokenExpiresAt;
    private String status;
    private String message;
}
