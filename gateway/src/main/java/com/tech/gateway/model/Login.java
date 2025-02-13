package com.tech.gateway.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record Login(
        @NotBlank(message = "username is required")
                @JsonProperty("email")
        String email,
        @NotBlank(message = "password is required")
        String password
) {
}
