package com.reciperestapi.reciperestapi.security.service.otp;

import com.reciperestapi.reciperestapi.common.settings.exceptions.LockedException;
import com.reciperestapi.reciperestapi.common.settings.exceptions.ResendOtpLimitException;
import com.reciperestapi.reciperestapi.common.settings.exceptions.ResourceNotFoundException;
import com.reciperestapi.reciperestapi.common.settings.exceptions.UsernameNotFoundException;
import com.reciperestapi.reciperestapi.security.dto.OtpValidationRequest;
import com.reciperestapi.reciperestapi.security.model.OtpResend;
import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.repository.impl_service.OtpDAOImpl;
import com.reciperestapi.reciperestapi.security.repository.impl_service.OtpResendDAOImpl;
import com.reciperestapi.reciperestapi.security.service.lock.UserStatusServiceImpl;
import com.reciperestapi.reciperestapi.user.model.User;
import com.reciperestapi.reciperestapi.user.repository.impl_service.UserDAOImpl;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import java.util.Optional;
import java.util.logging.Logger;

public class OtpResendService {
    @Inject
    private OtpDAOImpl otpDAO;
    @Inject
    private UserDAOImpl userDAO;
    @Inject
    private OtpResendDAOImpl otpResendDAO;
    @Inject
    private OtpServiceProvider otpServiceProvider;
    @Inject
    private UserStatusServiceImpl userStatusServiceImpl;
    @Inject
    private OtpResendServiceValidator otpResendServiceValidator;
    @Inject
    private OtpResendServiceManager otpResendServiceManager;

    private static final Logger LOGGER = Logger.getLogger(OtpResendService.class.getName());

    public OtpToken resendOtp(OtpValidationRequest otpValidationRequest) throws MessagingException, ResendOtpLimitException {
        Optional<OtpToken> otpTokenOptional = getOtpByRequestId(otpValidationRequest);
        OtpToken otpToken = otpTokenOptional
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired request identifier."));
        OtpResend otpResend = otpResendDAO.findByUserIdAndOtpType(otpToken).orElse(new OtpResend());
        User user = getUserByOtp(otpToken, otpResend);
        otpResendServiceValidator.validateResendAttempts(otpResend, user);
        otpResendServiceManager.saveOtpResend(otpResend, otpToken, user);
        OtpToken newOtpToken = otpServiceProvider.sendValidationOnEmail(otpToken.getUser(), otpToken.getOtpType());
        LOGGER.warning("newOtpToken " + newOtpToken);
        return newOtpToken;
    }

    private User getUserByOtp(OtpToken otpToken, OtpResend otpResend) {
        Optional<User> userOptional = userDAO.findUserById(otpToken.getUser())
                .flatMap(user -> {
                    if (userStatusServiceImpl.isAccountCurrentlyLocked(user)) {
                        throw new LockedException("Your account is temporarily locked until " + user.getLockExpiresAt());
                    }
                    if(userStatusServiceImpl.isAccountUnlocked(user)){
                        if (otpResend != null) {
                            otpResendServiceManager.resetOtpResendCount(otpResend);
                        }
                        if(user.getTotalBlocks() >= 2){
                            userStatusServiceImpl.unlockAccount(user);
                        }
                        userStatusServiceImpl.resetAccount(user);
                    }

                    return Optional.of(user);
                });
        return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    private Optional<OtpToken> getOtpByRequestId(OtpValidationRequest otpValidationRequest){
        return otpDAO.findByRequestId(otpValidationRequest.getUuid())
                .filter(otpToken -> otpToken.getValidatedAt() == null && !otpToken.isRedeemed())
                .flatMap(otpToken -> otpDAO.findByOtpType(otpToken));
    }
}
