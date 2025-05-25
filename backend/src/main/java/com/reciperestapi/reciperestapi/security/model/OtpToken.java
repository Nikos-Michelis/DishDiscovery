package com.reciperestapi.reciperestapi.security.model;

import com.reciperestapi.reciperestapi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OtpToken {
    private Integer otpId;
    private String otpUUId;
    private Integer userId;
    private String otpCode;
    private OtpType otpType;
    private boolean isRedeemed;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;
    private int otpResendCount;
    private User user;
}
