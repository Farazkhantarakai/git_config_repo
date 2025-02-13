package com.tech.gateway.services.EmailService;

public interface OtpService {
     String generateOtp(String email);
     boolean validateOtp(String email, String otp);
}
