package com.example.mooc.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class CourseModel {
    private Long id;
    @NotBlank private String title;
    @NotBlank private String description;
    @NotNull @Min(1) private Integer weeks;
    @NotNull private BigDecimal tuition;
    @NotNull private MinimumSkill minimumSkill;
    @NotNull private Long bootcampId;
    @NotNull private Long userId;
}
