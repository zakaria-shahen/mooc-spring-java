package com.example.mooc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    @NotBlank private String name;
    @NotBlank private String email;
    private boolean status;
    private Integer loginAttempts;
    private String role;

}
