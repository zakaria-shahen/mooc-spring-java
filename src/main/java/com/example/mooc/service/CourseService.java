package com.example.mooc.service;

import com.example.mooc.dto.CourseDto;
import com.example.mooc.mapping.CourseModelToDtoMapper;
import com.example.mooc.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CourseService {

    private CourseRepository courseRepository;

    public CourseDto fetchById(Long courseId) {
        var model = courseRepository.findById(courseId);
        return CourseModelToDtoMapper.INSTANCE.toDto(model);
    }

    public List<CourseDto> fetchAll(Long bootcampId, Pageable pageable) {
        var model = courseRepository.findAllByBootcampId(bootcampId, pageable);
        return CourseModelToDtoMapper.INSTANCE.toDto(model);
    }

    public CourseDto create(CourseDto courseDto, boolean isAdmin) {
        var model = CourseModelToDtoMapper.INSTANCE.toModel(courseDto);
        model = courseRepository.create(model, isAdmin);
        return CourseModelToDtoMapper.INSTANCE.toDto(model);

    }

    public Boolean delete(Long courseId, Long userId, Boolean isAdmin) {
        return courseRepository.delete(courseId, userId, isAdmin);
    }

    public CourseDto update(CourseDto courseDto, Boolean isAdmin) {
         var model = CourseModelToDtoMapper.INSTANCE.toModel(courseDto);
         model = courseRepository.update(model, isAdmin);
         return CourseModelToDtoMapper.INSTANCE.toDto(model);
    }
}
