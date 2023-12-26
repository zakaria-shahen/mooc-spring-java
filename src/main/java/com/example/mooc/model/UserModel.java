package com.example.mooc.model;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class UserModel {
    private Long id;
    @NotBlank private String name;
    @NotBlank private String email;
    private String password;
    private boolean status;
    private Integer loginAttempts;
    private String role;
}
