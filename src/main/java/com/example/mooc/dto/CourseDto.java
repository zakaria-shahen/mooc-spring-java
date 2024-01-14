package com.example.mooc.dto;

import com.example.mooc.model.CourseModel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class CourseDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank private String description;
    @NotNull
    @Min(1) private Integer weeks;
    @NotNull private BigDecimal tuition;
    @NotNull private CourseModel.MinimumSkill minimumSkill;
    @NotNull private Long bootcampId;
    private Long userId;
}
