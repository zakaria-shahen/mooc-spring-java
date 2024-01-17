package com.example.mooc.service;


import com.example.mooc.dto.BootcampDto;
import com.example.mooc.dto.response.BootcampFullDto;
import com.example.mooc.mapping.BootcampModelDtoMapper;
import com.example.mooc.mapping.CourseModelToDtoMapper;
import com.example.mooc.model.BootcampModel;
import com.example.mooc.repository.BootcampCareerRepository;
import com.example.mooc.repository.BootcampRepository;
import com.example.mooc.repository.CourseRepository;
import com.example.mooc.repository.impl.interceptors.FilterBy;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BootcampService {

    private BootcampRepository bootcampRepository;
    private BootcampCareerRepository bootcampCareerRepository;
    private CourseRepository courseRepository;

    public BootcampDto create(BootcampDto bootcampDto) {
        BootcampModel model = BootcampModelDtoMapper.INSTANCE.toModel(bootcampDto);
        model = bootcampRepository.create(model);
        return BootcampModelDtoMapper.INSTANCE.toDto(model);
    }

    public BootcampDto updateBootcamp(BootcampDto bootcampDto) {
        var model = BootcampModelDtoMapper.INSTANCE.toModel(bootcampDto);
        model = bootcampRepository.update(model);
        return BootcampModelDtoMapper.INSTANCE.toDto(model);
    }

    public List<BootcampDto> findAll(Pageable pageable, FilterBy filterBy) {
        var bootcampModelList = bootcampRepository.findAll(pageable, filterBy);
        return BootcampModelDtoMapper.INSTANCE.toDto(bootcampModelList);
    }

    @Deprecated
    public BootcampDto findById(Long id) {
        var model = bootcampRepository.findById(id);
        return BootcampModelDtoMapper.INSTANCE.toDto(model);
    }

    public BootcampFullDto findByIdWithFullInfo(Long id) {
        var bootcampModel = bootcampRepository.findById(id);
        var courseModelList = courseRepository.findAllByBootcampId(id, Pageable.unpaged());
        var careerModelList = bootcampCareerRepository.findAllByBootcampId(id);
        var bootcampFullDto = BootcampModelDtoMapper.INSTANCE.toFullDto(bootcampModel);

        bootcampFullDto.setCourseDtoList(CourseModelToDtoMapper.INSTANCE.toDto(courseModelList));
        bootcampFullDto.setCareerModelList(careerModelList);
        return bootcampFullDto;
    }

    public Boolean deleteById(Long id, Long userId, boolean isAdmin) {
        return bootcampRepository.delete(id, userId, isAdmin);
    }

}
