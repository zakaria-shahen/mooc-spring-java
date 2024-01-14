package com.example.mooc.mapping;

import com.example.mooc.dto.CourseDto;
import com.example.mooc.model.CourseModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CourseModelToDtoMapper {
    CourseModelToDtoMapper INSTANCE = Mappers.getMapper(CourseModelToDtoMapper.class);

    CourseDto toDto(CourseModel model);

    List<CourseDto> toDto(List<CourseModel> model);

    CourseModel toModel(CourseDto courseDto);
}
