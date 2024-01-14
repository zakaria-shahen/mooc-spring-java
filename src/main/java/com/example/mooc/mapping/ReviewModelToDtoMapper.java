package com.example.mooc.mapping;

import com.example.mooc.dto.ReviewDto;
import com.example.mooc.model.ReviewModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReviewModelToDtoMapper {
    ReviewModelToDtoMapper INSTANCE = Mappers.getMapper(ReviewModelToDtoMapper.class);

    ReviewDto toDto(ReviewModel model);

    List<ReviewDto> toDto(List<ReviewModel> model);

    ReviewModel toModel(ReviewDto reviewDto);
}
