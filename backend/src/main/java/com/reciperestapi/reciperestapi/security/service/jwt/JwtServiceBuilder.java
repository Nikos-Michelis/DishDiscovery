package com.reciperestapi.reciperestapi.security.service.jwt;

import com.reciperestapi.reciperestapi.security.model.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.PrivateKey;
import java.util.Date;

public class JwtServiceBuilder {

    public String buildToken(Token token, PrivateKey privateKey, String type) {
        return Jwts.builder()
                .setId(token.getJti())
                .claim("roles", token.getRoles())
                .claim("scope", type)
                .setSubject(token.getUserName())
                .setIssuedAt(Date.from(token.getIssuedAt().toInstant()))
                .setExpiration(Date.from(token.getExpiresAt().toInstant()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
