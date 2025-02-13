package com.tech.gateway.model;

import jakarta.validation.constraints.NotBlank;

public record ValidateOtp(
        @NotBlank
        String email,
        @NotBlank
        String otp,
        @NotBlank
        String newPassword
) {
}
