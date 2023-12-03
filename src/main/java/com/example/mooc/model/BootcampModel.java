package com.example.mooc.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class BootcampModel {
    private Long id;
    private String name;
    private String description;
    private String website;
    private String phone;
    private String email;
    private String address;
    private boolean housing;
    private boolean jobAssistance;
    private boolean jobGuarantee;
    private BigDecimal averageCost;
    private Float averageRating;
    private Long userId;
}
