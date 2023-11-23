package com.example.mooc.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class ReviewModel {
    private Long id;
    @NotBlank private String title;
    @NotBlank private String text;
    @NotNull @Min(0) @Max(5) Integer rating;
    @NotNull private Long courseId;
    @NotNull private Long userId;

}
