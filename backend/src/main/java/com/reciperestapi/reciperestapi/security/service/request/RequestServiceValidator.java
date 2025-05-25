package com.reciperestapi.reciperestapi.security.service.request;

import com.reciperestapi.reciperestapi.common.settings.exceptions.ResendOtpLimitException;
import com.reciperestapi.reciperestapi.common.settings.exceptions.ResourceNotFoundException;
import com.reciperestapi.reciperestapi.security.dto.OtpValidationRequest;
import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.repository.impl_service.OtpDAOImpl;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;

public class RequestServiceValidator {
    @Inject
    private OtpDAOImpl otpDAO;
    public String validateRequestId(OtpValidationRequest otpValidationRequest) throws ResendOtpLimitException {
        Optional<OtpToken> otpByRequestId = getOtpByRequestId(otpValidationRequest);
        if(otpByRequestId.isPresent()){
            return "Valid request id. Please submit your otp code to complete the authorization.";
        }else {
            throw new ResourceNotFoundException("Invalid or expired request identifier.");
        }
    }
    private Optional<OtpToken> getOtpByRequestId(OtpValidationRequest otpValidationRequest){
        return otpDAO.findByRequestId(otpValidationRequest.getUuid())
                .filter(otpToken -> !otpToken.getExpiresAt().isBefore(LocalDateTime.now()) && otpToken.getValidatedAt() == null && !otpToken.isRedeemed())
                .flatMap(otpToken -> otpDAO.findByOtpType(otpToken));
    }
}
