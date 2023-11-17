package com.example.mooc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class BootcampModel {
    private Long id;
    @NotBlank private String name;
    @NotBlank private String description;
    @NotBlank private String website;
    @NotBlank private String phone;
    @NotBlank private String email;
    @NotBlank private String address;
    // @NonNull
    private boolean housing;
    private boolean jobAssistance;
    private boolean jobGuarantee;
    private BigDecimal averageCost;
    private Float averageRating;
    @NonNull private Long userId;
}
