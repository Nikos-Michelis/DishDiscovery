package com.reciperestapi.reciperestapi.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpValidationRequest {
    private String otp;
    private String uuid;
    private boolean rememberMe;
}
