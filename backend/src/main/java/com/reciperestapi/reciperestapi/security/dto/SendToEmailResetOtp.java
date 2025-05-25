package com.reciperestapi.reciperestapi.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendToEmailResetOtp {
    @NotBlank(message = "Please enter your email address.")
    private String email;
}
