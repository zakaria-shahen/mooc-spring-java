package com.example.mooc.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RestCredentialsRequest(
        @NotBlank
        @Email
        String receiver,
        String receiverType
) {
}
