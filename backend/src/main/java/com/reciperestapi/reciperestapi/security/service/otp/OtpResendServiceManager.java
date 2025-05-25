package com.reciperestapi.reciperestapi.security.service.otp;

import com.reciperestapi.reciperestapi.security.model.OtpResend;
import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.repository.impl_service.OtpResendDAOImpl;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class OtpResendServiceManager {
    @Inject
    private OtpResendDAOImpl otpResendDAO;
    private static final Logger LOGGER = Logger.getLogger(OtpResendServiceManager.class.getName());

    public void saveOtpResend(OtpResend otpResend, OtpToken otpToken, User user) {
        OtpResend.builder().userId(user.getUserId());
        otpResend.setUserId(user.getUserId());
        otpResend.setOtpType(otpToken.getOtpType().name());
        otpResend.setOtpResendCount(otpResend.getOtpResendCount() + 1);
        otpResend.setLastOtpSentTime(LocalDateTime.now());
        saveOtpResendInfo(otpToken, otpResend);
        LOGGER.info("Recorded OTP resend for user " + otpResend.getUserId());

    }
    public void resetOtpResendCount(OtpResend otpResend) {
        otpResend.setOtpResendCount(0);
        LOGGER.info("OtpResend --> " + otpResend);
        otpResendDAO.update(otpResend);
        LOGGER.info("Reset OTP resend count for user " + otpResend.getUserId());
    }
    private void saveOtpResendInfo(OtpToken otpToken, OtpResend otpResend){
        otpResendDAO.findByUserIdAndOtpType(otpToken).ifPresent(existOtp -> otpResendDAO.delete(existOtp));
        otpResendDAO.save(otpResend);
    }

}
