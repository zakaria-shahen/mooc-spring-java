package com.example.mooc.repository;

import com.example.mooc.model.CourseModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseRepository {

    CourseModel create(CourseModel courseModel, Boolean isAdmin);

    CourseModel update(CourseModel courseModel, Boolean isAdmin);

    Boolean delete(CourseModel courseModel);

    Boolean delete(Long courseId, Long userId, Boolean isAdmin);

    List<CourseModel> findAllByBootcampId(Long bootcampId, Pageable pageable);

    CourseModel findById(Long id);
}
