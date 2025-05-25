package com.reciperestapi.reciperestapi.security.service.otp;

import com.reciperestapi.reciperestapi.security.model.OtpType;
import com.reciperestapi.reciperestapi.security.repository.impl_service.OtpDAOImpl;
import com.reciperestapi.reciperestapi.security.email.EmailTemplateService;
import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;

import java.util.logging.Logger;

public class OtpServiceProvider {
    private String confirmationUrl = "http://localhost:63342/VanillaJsFormValidation/validate-otp.html";

    @Inject
    private OtpDAOImpl otpDAO;
    @Inject
    private EmailTemplateService emailTemplateService;
    @Inject
    private OtpServiceBuilder otpServiceBuilder;

    public OtpToken sendValidationOnEmail(User user, OtpType otpType) throws MessagingException {
        OtpToken otp = otpServiceBuilder.generateOtpCode(user, otpType);
        saveOtpToken(otp);
        emailTemplateService.sendAuthenticationEmailToClient(user.getEmail(), user.getUsername(), confirmationUrl, otp.getOtpCode());
        return otp;
    }

    private void saveOtpToken(OtpToken otp){
        otpDAO.findByOtpType(otp).ifPresent(existOtp -> otpDAO.delete(existOtp));
        otpDAO.saveOtp(otp);
    }
}