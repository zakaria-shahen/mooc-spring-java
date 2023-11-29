package com.example.mooc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CareerModel {
    private Long id;
    @NotBlank private String name;
}
