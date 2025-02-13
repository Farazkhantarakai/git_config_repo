package com.tech.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RegisterUser(
       @NotBlank
               @JsonProperty("username")
        String username,
        @NotBlank
                @JsonProperty("email")
        String email,
        @NotBlank
        String password,
        @NotBlank
        String roles) {

}
