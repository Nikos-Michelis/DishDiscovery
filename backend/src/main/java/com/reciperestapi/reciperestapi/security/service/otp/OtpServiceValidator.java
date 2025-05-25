package com.reciperestapi.reciperestapi.security.service.otp;

import com.reciperestapi.reciperestapi.common.settings.exceptions.InvalidOtpException;
import com.reciperestapi.reciperestapi.common.settings.exceptions.LockedException;
import com.reciperestapi.reciperestapi.security.dto.OtpValidationRequest;
import com.reciperestapi.reciperestapi.security.model.OtpResend;
import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.repository.OtpResendDAO;
import com.reciperestapi.reciperestapi.security.service.lock.UserStatusServiceImpl;
import com.reciperestapi.reciperestapi.user.model.User;
import com.reciperestapi.reciperestapi.user.repository.impl_service.UserDAOImpl;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

public class OtpServiceValidator {
    @Inject
    private UserDAOImpl userDAO;
    @Inject
    private OtpResendDAO otpResendDAO;
    @Inject
    private UserStatusServiceImpl userStatusServiceImpl;
    @Inject
    private OtpResendServiceManager otpResendServiceManager;

    private static final int MAX_ATTEMPTS = 3;


    public void isValidOtp(OtpToken otpToken, OtpValidationRequest otpValidationRequest, User savedUser) {
        if (userStatusServiceImpl.isAccountCurrentlyLocked(savedUser)) {
            throw new LockedException("Your account is temporary locked until " + savedUser.getLockExpiresAt());
        }

        if (otpToken.getOtpCode().equals(otpValidationRequest.getOtp()) && LocalDateTime.now().isBefore(otpToken.getExpiresAt())) {
            otpResendDAO.findByUserIdAndOtpType(otpToken)
                    .ifPresent(otpResend -> otpResendServiceManager.resetOtpResendCount(otpResend));
            userStatusServiceImpl.unlockAccount(savedUser);
        } else {
            handleInvalidOtp(savedUser);
        }
    }

    private void handleInvalidOtp(User savedUser) {
        savedUser.setTotalAttempts(savedUser.getTotalAttempts() + 1);
        userDAO.updateAttempts(savedUser);

        if (savedUser.getTotalAttempts() >= MAX_ATTEMPTS) {
            userStatusServiceImpl.lockAccount(savedUser);
            throw new InvalidOtpException("Invalid OTP. Your account has been locked due to multiple failed attempts. Try again in " + savedUser.getLockExpiresAt());
        } else {
            throw new InvalidOtpException("Invalid OTP. You have " + savedUser.getTotalAttempts() + "/" + MAX_ATTEMPTS + " attempts.");
        }
    }
}
