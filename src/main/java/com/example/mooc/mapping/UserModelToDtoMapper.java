package com.example.mooc.mapping;

import com.example.mooc.dto.UserDto;
import com.example.mooc.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserModelToDtoMapper {
    UserModelToDtoMapper INSTANCE = Mappers.getMapper(UserModelToDtoMapper.class);

    UserModel toModel(UserDto userDto);

    UserDto toDto(UserModel model);
}
