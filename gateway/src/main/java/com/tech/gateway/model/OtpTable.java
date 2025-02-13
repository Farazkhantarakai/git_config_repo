package com.tech.gateway.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Table
@Entity
public class OtpTable {
@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
Long id;
@Column(name = "EMAIL", nullable = false)
 String email;
@Column(name = "VERIFICATION")
Boolean  verified;
@Column(name = "OTP")
String otp;

    public LocalDateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    public String getOtp() {
        return otp;
    }

    public Boolean getVerified() {
        return verified;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    @Column(name = "OTP_EXPIRY")
LocalDateTime expiryDateTime;

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setExpiryDateTime(LocalDateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }
}
