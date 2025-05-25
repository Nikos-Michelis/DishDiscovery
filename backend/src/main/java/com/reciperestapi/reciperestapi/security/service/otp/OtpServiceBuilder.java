package com.reciperestapi.reciperestapi.security.service.otp;

import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.model.OtpType;
import com.reciperestapi.reciperestapi.security.service.request.RequestServiceBuilder;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.inject.Inject;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

public class OtpServiceBuilder {
    @Inject
    private RequestServiceBuilder requestServiceBuilder;
    private static final long OTP_EXPIRATION = 5; // 5 min
    private static final int OTP_LENGTH = 6;

    public OtpToken generateOtpCode(User user, OtpType otpType) {
        String generatedToken = generateActivationCode(OTP_LENGTH);
        return OtpToken.builder()
                .otpUUId(requestServiceBuilder.generateRequestId())
                .otpCode(generatedToken)
                .otpType(otpType)
                .isRedeemed(false)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRATION))
                .user(user)
                .build();
    }
    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

}
