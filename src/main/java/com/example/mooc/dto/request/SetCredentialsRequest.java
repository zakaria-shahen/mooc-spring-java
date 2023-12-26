package com.example.mooc.dto.request;

import jakarta.validation.constraints.*;

public record SetCredentialsRequest(
        @NotBlank
        @Email
        String receiver,
        @NotBlank
        String credentials,
        @NotBlank
        @Min(4)
        @Max(4)
        String otp,
        @NotNull
        Long otpId
) {
}
