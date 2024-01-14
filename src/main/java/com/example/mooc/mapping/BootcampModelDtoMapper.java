package com.example.mooc.mapping;

import com.example.mooc.dto.response.BootcampDto;
import com.example.mooc.model.BootcampModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BootcampModelDtoMapper {
    BootcampModelDtoMapper INSTANCE = Mappers.getMapper(BootcampModelDtoMapper.class);

    List<BootcampDto> toDto(List<BootcampModel> bootcampModelList);

    BootcampDto toDto(BootcampModel model);

    BootcampModel toModel(BootcampDto bootcampDto);
}
