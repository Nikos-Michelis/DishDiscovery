package com.reciperestapi.reciperestapi.security.model;

import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpResend {
    private int resendId;
    private int userId;
    private String otpType;
    private int otpResendCount;
    private LocalDateTime lastOtpSentTime;
}
