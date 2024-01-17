package com.example.mooc.dto.response;

import com.example.mooc.dto.CourseDto;
import com.example.mooc.model.CareerModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class BootcampFullDto {
    private Long id;
    @NotBlank
    private String name;
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
    @NotNull
    private Long userId;
    private List<CareerModel> careerModelList;
    private List<CourseDto> courseDtoList;

}