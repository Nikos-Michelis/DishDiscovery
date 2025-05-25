package com.reciperestapi.reciperestapi.security.service.cookie;

import jakarta.ws.rs.core.NewCookie;
public class CookieServiceBuilder {

    public NewCookie buildCookie(String tokenType, String token, int maxAge){
        return new NewCookie
                .Builder(tokenType)
                .value(token)
                .path("/")
                .maxAge(maxAge)
                .secure(true)
                .httpOnly(true)
                .build();
    }
}
