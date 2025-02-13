package com.tech.gateway.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtp(
       @NotBlank
       @Email
        String email,
             @NotBlank
                        String otp) {

}
