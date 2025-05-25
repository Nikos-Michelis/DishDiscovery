package com.reciperestapi.reciperestapi.security.service;

import com.reciperestapi.reciperestapi.common.settings.exceptions.UsernameNotFoundException;
import com.reciperestapi.reciperestapi.security.auth.CustomSecurityContext;
import com.reciperestapi.reciperestapi.security.dto.*;
import com.reciperestapi.reciperestapi.security.model.OtpType;
import com.reciperestapi.reciperestapi.security.repository.impl_service.OtpDAOImpl;
import com.reciperestapi.reciperestapi.security.model.OtpToken;
import com.reciperestapi.reciperestapi.security.model.Roles;
import com.reciperestapi.reciperestapi.security.model.Token;
import com.reciperestapi.reciperestapi.security.repository.impl_service.TokenDAOImpl;
import com.reciperestapi.reciperestapi.common.settings.exceptions.InvalidAuthenticationTokenException;
import com.reciperestapi.reciperestapi.security.service.jwt.JwtServiceParser;
import com.reciperestapi.reciperestapi.security.service.jwt.JwtServiceProvider;
import com.reciperestapi.reciperestapi.security.service.otp.OtpServiceProvider;
import com.reciperestapi.reciperestapi.security.service.otp.OtpServiceValidator;
import com.reciperestapi.reciperestapi.security.service.password.PasswordEncoder;
import com.reciperestapi.reciperestapi.user.repository.impl_service.UserDAOImpl;
import com.reciperestapi.reciperestapi.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class AuthenticationService {
    @Inject
    private UserDAOImpl userDAO;
    @Inject
    private TokenDAOImpl tokenDAO;
    @Inject
    private OtpDAOImpl otpDAO;
    @Inject
    private JwtServiceProvider jwtTokenProvider;
    @Inject
    private JwtServiceParser jwtServiceParser;
    @Inject
    private UserCredentialsValidator userCredentialsValidator;
    @Inject
    private PasswordEncoder passwordEncoder;
    @Inject
    private OtpServiceProvider otpServiceProvider;
    @Inject
    private OtpServiceValidator otpServiceValidator;

    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }

    public OtpToken requestLogin(AuthenticationRequest credentials) throws MessagingException {
        LOGGER.info(String.format("credentials: " + credentials));
        User user = userCredentialsValidator.validateCredentials(credentials.getUsername(), credentials.getPassword());
        return otpServiceProvider.sendValidationOnEmail(user, OtpType.LOGIN);
    }
    @Transactional
    public ResponseTokenDetails proceedOtpLogin(OtpValidationRequest otpValidationRequest) {
        OtpToken savedOtpToken = otpDAO.findByRequestId(otpValidationRequest.getUuid())
                .filter(otp -> otp.getValidatedAt() == null && !otp.isRedeemed())
                .orElseThrow(() -> new InvalidAuthenticationTokenException("Invalid request id."));
        User savedUser = userDAO.findUserById(savedOtpToken.getUser().getUserId())
                .filter(User::isEnable)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        otpServiceValidator.isValidOtp(savedOtpToken, otpValidationRequest, savedUser);

        savedOtpToken.setValidatedAt(LocalDateTime.now());
        savedOtpToken.setRedeemed(true);
        otpDAO.update(savedOtpToken);

        Token accessToken = jwtTokenProvider.generateAccessToken(savedUser);
        Token refreshToken = jwtTokenProvider.generateRefreshToken(savedUser, otpValidationRequest.isRememberMe());
        revokeAllUserTokens(savedUser);
        tokenDAO.saveToken(refreshToken);
        return ResponseTokenDetails.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .userName(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(savedUser.getRole())
                .accessTokenIssuedAt(accessToken.getIssuedAt())
                .accessTokenExpiresAt(accessToken.getExpiresAt())
                .refreshTokenIssuedAt(refreshToken.getIssuedAt())
                .refreshTokenExpiresAt(refreshToken.getExpiresAt())
                .status(Response.Status.OK.getReasonPhrase())
                .message("Login Successful.")
                .build();

    }
    public OtpToken requestRegister(AuthenticationRequest credentials) throws MessagingException {
        var user = User.builder()
                .userUUId(generateTokenIdentifier())
                .username(credentials.getUsername())
                .email(credentials.getEmail())
                .password(passwordEncoder.hashPassword(credentials.getPassword()))
                .accountLocked(false)
                .enable(false)
                .roleId(new HashSet<>(2))
                .role(new HashSet<>(Collections.singleton(Roles.USER)))
                .build();

        User savedUser = userDAO.save(user).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        LOGGER.info(String.format("savedUser: " + savedUser));
        return otpServiceProvider.sendValidationOnEmail(savedUser, OtpType.REGISTER);
    }
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenDAO.findAllValidTokenByUserId(user.getUserId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenDAO.update(validUserTokens);
    }
    public ResponseTokenDetails refreshToken(SecurityContext securityContext) {
        final String userName;
        final Token refreshToken = ((CustomSecurityContext) securityContext).getToken();
        userName = jwtServiceParser.parseToken(refreshToken.getToken()).getUserName();
        if (userName != null) {
            var user = userDAO.findByUsername(userName)
                    .filter(User::isEnable)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            var isRefreshTokenValid = tokenDAO.findByToken(refreshToken.getToken())
                    .map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
            if (jwtTokenProvider.isTokenValid(refreshToken.getToken(), user.getUsername()) && isRefreshTokenValid) {
               Token accessToken = jwtTokenProvider.generateAccessToken(user);
                return ResponseTokenDetails.builder()
                        .accessToken(accessToken.getToken())
                        .refreshToken(refreshToken.getToken())
                        .userName(user.getUsername())
                        .roles(user.getRole())
                        .accessTokenIssuedAt(accessToken.getIssuedAt())
                        .accessTokenExpiresAt(accessToken.getExpiresAt())
                        .status(Response.Status.OK.getReasonPhrase())
                        .message("The token has refreshed successfully.")
                        .build();
            } else {
                throw new InvalidAuthenticationTokenException("Invalid or expired token.");
            }
        } else {
            throw new InvalidAuthenticationTokenException("Invalid token or security context already set.");
        }
    }
    @Transactional
    public ResponseTokenDetails activateAccount(OtpValidationRequest otpValidationRequest){
        OtpToken savedOtpToken = otpDAO.findByRequestId(otpValidationRequest.getUuid())
                .filter(otp -> otp.getValidatedAt() == null && !otp.isRedeemed())
                .orElseThrow(() -> new InvalidAuthenticationTokenException("Invalid request id."));
        var savedUser = userDAO.findUserById(savedOtpToken.getUser().getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        otpServiceValidator.isValidOtp(savedOtpToken, otpValidationRequest, savedUser);

        savedOtpToken.setValidatedAt(LocalDateTime.now());

        savedUser.setEnable(true);
        userDAO.updateStatus(savedUser);
        savedOtpToken.setValidatedAt(LocalDateTime.now());
        savedOtpToken.setRedeemed(true);
        LOGGER.info("saveOtpToken -> " + savedOtpToken);
        otpDAO.update(savedOtpToken);
        Token accessToken = jwtTokenProvider.generateAccessToken(savedUser);
        Token refreshToken = jwtTokenProvider.generateRefreshToken(savedUser, otpValidationRequest.isRememberMe());
        revokeAllUserTokens(savedUser);
        tokenDAO.saveToken(refreshToken);
        return ResponseTokenDetails.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .userName(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(savedUser.getRole())
                .accessTokenIssuedAt(accessToken.getIssuedAt())
                .accessTokenExpiresAt(accessToken.getExpiresAt())
                .refreshTokenIssuedAt(refreshToken.getIssuedAt())
                .refreshTokenExpiresAt(refreshToken.getExpiresAt())
                .status(Response.Status.OK.getReasonPhrase())
                .message("Your account activated successfully")
                .build();
    }
    @Transactional
    public OtpToken requestPasswordReset(String email) throws MessagingException {
        var savedUser = userDAO.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("We cannot find an account with that e-mail address."));
       return otpServiceProvider.sendValidationOnEmail(savedUser, OtpType.RESET_PASSWORD);
    }
    @Transactional
    public void proceedOtpResetPassword(OtpValidationRequest otpValidationRequest) {
        OtpToken savedOtpToken = otpDAO.findByRequestId(otpValidationRequest.getUuid())
                .filter(otp -> otp.getValidatedAt() == null && !otp.isRedeemed())
                .orElseThrow(() -> new InvalidAuthenticationTokenException("Invalid request id."));
        var savedUser = userDAO.findUserById(savedOtpToken.getUser().getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        otpServiceValidator.isValidOtp(savedOtpToken, otpValidationRequest, savedUser);

        savedOtpToken.setValidatedAt(LocalDateTime.now());
        otpDAO.update(savedOtpToken);
    }

    @Transactional
    public void passwordReset(ResetPasswordRequest request) {
        OtpToken savedOtpToken = otpDAO.findByRequestId(request.getUuid())
                .filter(otpToken -> otpToken.getValidatedAt() != null && !otpToken.isRedeemed())
                .orElseThrow(() -> new InvalidAuthenticationTokenException("Invalid request id."));

        var savedUser = userDAO.findUserById(savedOtpToken.getUser().getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        // Ensure the OTP token has been validated and used for its intended purpose
        if (savedOtpToken.getValidatedAt() != null && savedOtpToken.isRedeemed()) {
            throw new InvalidAuthenticationTokenException("OTP token is not validated or already used.");
        }
        OtpValidationRequest otpValidationRequest = OtpValidationRequest.builder().uuid(request.getUuid()).otp(savedOtpToken.getOtpCode()).build();
        otpServiceValidator.isValidOtp(savedOtpToken, otpValidationRequest, savedUser);

        savedOtpToken.setRedeemed(true);
        otpDAO.update(savedOtpToken);
        savedUser.setPassword(passwordEncoder.hashPassword(request.getConfirmPassword()));
        userDAO.update(savedUser);
    }

    public User getAuthUserDetails(SecurityContext securityContext) {
        final Token token = ((CustomSecurityContext) securityContext).getToken();
        LOGGER.info("token -> " + token);
        if (securityContext.getUserPrincipal() != null) {
            return userDAO.findByUsername(token.getUserName())
                    .filter(User::isEnable)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            throw new InvalidAuthenticationTokenException("Missing or invalid Authorization header.");
        }
    }
}
