package com.example.mooc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class OtpModel {

    private long id;
    @NotBlank
    private String receiver;
    @NotBlank
    private String otp;
    private boolean used;
    private int attempts;
    @NotNull
    private String type;
    private long createAt;
    private long expirationAfterSeconds;

    public enum OtpType {
         RESET_CREDENTIALS
    }

}
