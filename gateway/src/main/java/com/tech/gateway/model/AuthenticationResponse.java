package com.tech.gateway.model;



public class AuthenticationResponse {
private String accessToken;
private String refreshToken;
private String message;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthenticationResponse(String accessToken, String refreshToken, String message) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.message = message;
}

}
