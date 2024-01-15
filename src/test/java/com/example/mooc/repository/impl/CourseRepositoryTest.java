package com.example.mooc.repository.impl;

import com.example.mooc.mapping.CourseModelToDtoMapper;
import com.example.mooc.model.CourseModel;
import com.example.mooc.repository.CourseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

@SpringBootTest
@Testcontainers(parallel = true)
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void testing() {
        var model = courseRepository.create(CourseModel.builder()
                        .title("dfkjdf")
                        .description("dfidf")
                        .weeks(1)
                        .tuition(BigDecimal.ONE)
                        .minimumSkill(CourseModel.MinimumSkill.ADVANCED.name())
                        .cost(BigDecimal.ONE)
                        .userId(1L)
                        .bootcampId(1L)
                .build(), true);
         var dto = CourseModelToDtoMapper.INSTANCE.toDto(model);

        Assertions.assertThat(dto).isNotNull();
    }
}