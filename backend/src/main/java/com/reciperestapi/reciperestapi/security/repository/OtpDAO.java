package com.reciperestapi.reciperestapi.security.repository;

import com.reciperestapi.reciperestapi.security.dto.OtpValidationRequest;
import com.reciperestapi.reciperestapi.security.model.OtpResend;
import com.reciperestapi.reciperestapi.security.model.OtpToken;

import java.util.Optional;

public interface OtpDAO {
    void saveOtp(OtpToken otpToken);
    Optional<OtpToken> findByOtp(OtpValidationRequest otpToken);
    Optional<OtpToken> findByRequestId(String requestId);
    void update(OtpToken otpToken);
    void updateRedeemOtp(OtpToken otpToken);
    Optional<OtpToken> findByOtpType(OtpToken otp);
    void delete(OtpToken otpToken);
}
