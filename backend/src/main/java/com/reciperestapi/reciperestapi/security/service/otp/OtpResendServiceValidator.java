package com.reciperestapi.reciperestapi.security.service.otp;

import com.reciperestapi.reciperestapi.common.settings.exceptions.ResendOtpLimitException;
import com.reciperestapi.reciperestapi.security.model.OtpResend;
import com.reciperestapi.reciperestapi.security.service.lock.UserStatusService;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

public class OtpResendServiceValidator {
    @Inject
    private UserStatusService userStatusService;
    private static final int MAX_RESEND_ATTEMPTS = 3; // Maximum allowed resend attempts
    private static final int RESEND_COOLDOWN_SECONDS = 5; // Cooldown period between resends (in seconds)
    private static final Logger LOGGER = Logger.getLogger(OtpResendServiceValidator.class.getName());



    public void validateResendAttempts(OtpResend otpResend, User user) {
        if (isExceedMaxAttempts(otpResend, user)) {
            LOGGER.warning("User " + otpResend.getUserId() + " has exceeded the maximum resend attempts.");
            throw new ResendOtpLimitException("Exceeded maximum resend attempts.");
        }

        if (isInCooldownPeriod(otpResend)) {
            LOGGER.warning("User " + otpResend.getUserId() + " is within the cooldown period for resending OTP.");
            throw new ResendOtpLimitException(" Wait until exceeded the cooldown period " + RESEND_COOLDOWN_SECONDS + " seconds.");
        }
    }

    public boolean isExceedMaxAttempts(OtpResend otpResend, User user) {
        if (otpResend.getOtpResendCount() >= MAX_RESEND_ATTEMPTS) {
            userStatusService.lockAccount(user);
            return true;
        }
        return false;
    }

    public boolean isInCooldownPeriod(OtpResend otpResend) {
        if (otpResend.getLastOtpSentTime() != null) {
            long secondsSinceLastOtp = ChronoUnit.SECONDS.between(otpResend.getLastOtpSentTime(), LocalDateTime.now());
            if (secondsSinceLastOtp < RESEND_COOLDOWN_SECONDS) {
                LOGGER.warning("User " + otpResend.getUserId() + " is within the cooldown period for resending OTP.");
                return true;
            }
        }
        return false;
    }
}
