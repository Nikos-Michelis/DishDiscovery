package com.reciperestapi.reciperestapi.security.service.cookie;

import com.reciperestapi.reciperestapi.security.dto.ResponseTokenDetails;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.NewCookie;

public class CookieServiceProvider {
    @Inject
    private CookieServiceBuilder cookieServiceBuilder;
    //private static final long COOKIE_EXPIRATION = 60 * 60; // 1 hour

    public NewCookie buildAccessTokenCookie(ResponseTokenDetails responseTokenDetails) {
        int maxAge = calculateCookieMaxAge(responseTokenDetails.getAccessTokenExpiresAt().toInstant().toEpochMilli(),
                responseTokenDetails.getAccessTokenIssuedAt().toInstant().toEpochMilli());
        System.out.println(maxAge);
        return cookieServiceBuilder.buildCookie("access_token", responseTokenDetails.getAccessToken(), maxAge);
    }

    public NewCookie buildRefreshTokenCookie(ResponseTokenDetails responseTokenDetails) {
        int maxAge = calculateCookieMaxAge(responseTokenDetails.getRefreshTokenExpiresAt().toInstant().toEpochMilli(),
                responseTokenDetails.getRefreshTokenIssuedAt().toInstant().toEpochMilli());
        System.out.println(maxAge);
        return cookieServiceBuilder.buildCookie("refresh_token", responseTokenDetails.getRefreshToken(), maxAge);
    }

    public NewCookie clearAccessTokenCookie() {return cookieServiceBuilder.buildCookie("access_token", null, 0);}
    public NewCookie clearRefreshTokenCookie() {return cookieServiceBuilder.buildCookie("refresh_token", null, 0);}
    public int calculateCookieMaxAge(long expire, long issue){
        return (int) ((expire - issue) / 1000);
    }


}
