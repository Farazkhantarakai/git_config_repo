package com.tech.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record ResetPassword(
        @NotNull
         @JsonProperty("email")
        String email
) {
}
