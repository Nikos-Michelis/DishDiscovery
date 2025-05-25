package com.reciperestapi.reciperestapi.security.repository;

import com.reciperestapi.reciperestapi.security.model.OtpResend;
import com.reciperestapi.reciperestapi.security.model.OtpToken;

import java.util.Optional;

public interface OtpResendDAO {
    Optional<OtpResend> findByUserIdAndOtpType(OtpToken otpToken);
    void save(OtpResend otpResend);
    void update(OtpResend otpResend);
    void delete(OtpResend otpResend);
}
