package com.example.mooc.repository;

import com.example.mooc.model.CourseModel;

import java.util.List;

public interface CourseRepository {

    CourseModel create(CourseModel courseModel);

    CourseModel update(CourseModel courseModel);

    Boolean delete(CourseModel courseModel);

    Boolean delete(Long courseId, Long userId);

    List<CourseModel> findAllByBootcampId(Long bootcampId);

    CourseModel findById(Long id);
}
