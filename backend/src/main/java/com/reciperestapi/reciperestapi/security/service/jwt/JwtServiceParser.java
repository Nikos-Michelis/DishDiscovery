package com.reciperestapi.reciperestapi.security.service.jwt;

import com.reciperestapi.reciperestapi.common.settings.exceptions.InvalidAuthenticationTokenException;
import com.reciperestapi.reciperestapi.security.model.Roles;
import com.reciperestapi.reciperestapi.security.model.Token;
import com.reciperestapi.reciperestapi.security.model.TokenScope;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.security.PrivateKey;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtServiceParser {
    private PrivateKey accessPrivateKey;
    private PrivateKey refreshPrivateKey;
    @Inject
    private ReadKeyService readPrivateKey;

    @PostConstruct
    protected void init() throws Exception {
        accessPrivateKey = readPrivateKey.readPrivateKey("/jwt-token/access-token/private_key_access.pem");
        refreshPrivateKey = readPrivateKey.readPrivateKey("/jwt-token/refresh-token/private_key_refresh.pem");
    }

    public Token parseToken(String token) {
        try {
            return Token.builder()
                    .jti(extractTokenIdFromToken(token))
                    .token(token)
                    .userName(extractUsernameFromToken(token))
                    .roles(extractRolesFromToken(token))
                    .tokenScope(TokenScope.valueOf(extractScopeFromToken(token)))
                    .issuedAt(extractIssuedDateFromToken(token))
                    .expiresAt(extractExpirationFromToken(token))
                    .build();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
            throw new InvalidAuthenticationTokenException("Invalid token", e);
        } catch (ExpiredJwtException e) {
            throw new InvalidAuthenticationTokenException("Expired token", e);
        } catch (InvalidClaimException e) {
            throw new InvalidAuthenticationTokenException("Invalid value for claim \"" + e.getClaimName() + "\"", e);
        } catch (Exception e) {
            throw new InvalidAuthenticationTokenException("Invalid token", e);
        }
    }

    private Claims extractClaim(String token, PrivateKey privateKey){
        return Jwts.parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Claims extractAllClaims(String token) {
        try {
            try {
                return extractClaim(token, accessPrivateKey);
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
                return extractClaim(token, refreshPrivateKey);
            }
        } catch (JwtException ex) {
            throw new InvalidAuthenticationTokenException("Invalid token signature.", ex);
        }
    }

    private String extractTokenIdFromToken(@NotNull String token) {
        return extractAllClaims(token).getId();
    }

    private String extractUsernameFromToken(@NotNull String token) {
        return extractAllClaims(token).getSubject();
    }

    private ZonedDateTime extractIssuedDateFromToken(@NotNull String token) {
        return ZonedDateTime.ofInstant(extractAllClaims(token).getIssuedAt().toInstant(), ZoneId.systemDefault());
    }

    private ZonedDateTime extractExpirationFromToken(@NotNull String token) {
        return ZonedDateTime.ofInstant(extractAllClaims(token).getExpiration().toInstant(), ZoneId.systemDefault());
    }

    private Set<Roles> extractRolesFromToken(@NotNull String token) {
        Claims claims = extractAllClaims(token);
        List<String> rolesAsString = claims.get("roles", List.class);
        // if roles doesn't set in token will return an empty list
        if (rolesAsString == null) {
            rolesAsString = new ArrayList<>();
        }
        return rolesAsString.stream().map(Roles::valueOf).collect(Collectors.toSet());
    }
    private String extractScopeFromToken(@NotNull String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("scope", String.class);
    }

}
