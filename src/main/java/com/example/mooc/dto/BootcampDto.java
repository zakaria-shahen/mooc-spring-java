package com.example.mooc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class BootcampDto {
    private Long id;
    @NotBlank private String name;
    @NotBlank private String description;
    @NotBlank private String website;
    @NotBlank private String phone;
    @NotBlank private String email;
    @NotBlank private String address;
    // @NotNull
    private boolean housing;
    private boolean jobAssistance;
    private boolean jobGuarantee;
    private BigDecimal averageCost;
    private Float averageRating;
    @NotNull private Long userId;

}

