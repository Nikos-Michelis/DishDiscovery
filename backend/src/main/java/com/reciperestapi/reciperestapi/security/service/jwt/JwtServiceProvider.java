package com.reciperestapi.reciperestapi.security.service.jwt;

import com.reciperestapi.reciperestapi.security.model.TokenScope;
import com.reciperestapi.reciperestapi.security.model.Token;
import com.reciperestapi.reciperestapi.security.model.TokenType;
import com.reciperestapi.reciperestapi.user.model.User;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Logger;

@ApplicationScoped
public class JwtServiceProvider {
    private PrivateKey accessPrivateKey;
    private PrivateKey refreshPrivateKey;
    private PublicKey accessPublicKey;
    private PublicKey refreshPublicKey;
    @Inject
    private ReadKeyService readPrivateKey;
    @Inject
    private JwtServiceBuilder jwtServiceBuilder;
    @Inject
    private JwtServiceParser jwtServiceParser;

    private static final long ACCESS_EXPIRATION = 15 * 60 * 1000; // 1 hour
    private static final long REFRESH_EXPIRATION = 3 * 60 * 60 * 1000; // 3 hours
    private static final long REFRESH_EXPIRATION_REMEMBER_ME = 30L * 24 * 60 * 60 * 1000; // 30 days
    private static final Logger LOGGER = Logger.getLogger(JwtServiceProvider.class.getName());


    @PostConstruct
    protected void init() throws Exception {
        accessPrivateKey = readPrivateKey.readPrivateKey("/jwt-token/access-token/private_key_access.pem");
        accessPublicKey = readPrivateKey.readPublicKey("/jwt-token/access-token/public_key_access.pem");
        refreshPrivateKey = readPrivateKey.readPrivateKey("/jwt-token/refresh-token/private_key_refresh.pem");
        refreshPublicKey = readPrivateKey.readPublicKey("/jwt-token/refresh-token/public_key_refresh.pem");
    }

    public Token generateAccessToken(User user) {
        ZonedDateTime issuedDate = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate, ACCESS_EXPIRATION);

        Token token = Token.builder()
                .userId(user.getUserId())
                .jti(generateTokenIdentifier())
                .roles(user.getRole())
                .issuedAt(issuedDate)
                .expiresAt(expirationDate)
                .userName(user.getUsername())
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        String generatedToken = jwtServiceBuilder.buildToken(token, accessPrivateKey, TokenScope.ACCESS.name());
        token.setToken(generatedToken);
        return token;
    }

    public Token generateRefreshToken(User user, boolean isRememberMe) {
        ZonedDateTime issuedDate = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime expirationDate = calculateRefreshTokenExpirationDate(issuedDate, isRememberMe);
        LOGGER.info("expirationDate --> " + expirationDate);
        Token token = Token.builder()
                .userId(user.getUserId())
                .jti(generateTokenIdentifier())
                .roles(user.getRole())
                .issuedAt(issuedDate)
                .expiresAt(expirationDate)
                .userName(user.getUsername())
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        String generatedToken = jwtServiceBuilder.buildToken(token, refreshPrivateKey, TokenScope.REFRESH.name());
        token.setToken(generatedToken);
        return token;
    }
    public Token parseToken(String token) {
        return jwtServiceParser.parseToken(token);
    }
    public boolean isTokenValid(String token, String username) {
        final Token tokenUsername = jwtServiceParser.parseToken(token);
        return tokenUsername.getUserName().equals(username) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(@NotNull String token) {
        return jwtServiceParser.parseToken(token).getExpiresAt().isBefore(ZonedDateTime.now());
    }

    public boolean isAccessToken(String token, TokenScope tokenScope){
        return tokenScope.equals(TokenScope.ACCESS) && verifyAccessToken(token);
    }

    public boolean isRefreshToken(String token, TokenScope tokenScope){
        return tokenScope.equals(TokenScope.REFRESH) && verifyRefreshToken(token);
    }

    private boolean verifyAccessToken(String token) {
        return verifyJWT(token, accessPublicKey, String.valueOf(TokenScope.ACCESS).toUpperCase());
    }
    private boolean verifyRefreshToken(String token) {
        return verifyJWT(token, refreshPublicKey, String.valueOf(TokenScope.REFRESH).toUpperCase());
    }

    private boolean verifyJWT(String token, PublicKey publicKey, String expectedType) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            String tokenType = claims.get("scope", String.class);
            LOGGER.info("token scope -> " + tokenType);
            return expectedType.equals(tokenType);
        } catch (JwtException e) {
            return false;
        }
    }
    private ZonedDateTime calculateExpirationDate(ZonedDateTime issuedDate, long expirationMillis) {
        return issuedDate.plusSeconds(expirationMillis / 1000);  // Convert milliseconds to seconds
    }

    private ZonedDateTime calculateRefreshTokenExpirationDate(ZonedDateTime issuedDate, boolean isRememberMe){
        return !isRememberMe ? calculateExpirationDate(issuedDate, REFRESH_EXPIRATION)
                : calculateExpirationDate(issuedDate, REFRESH_EXPIRATION_REMEMBER_ME);
    }

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }

}
