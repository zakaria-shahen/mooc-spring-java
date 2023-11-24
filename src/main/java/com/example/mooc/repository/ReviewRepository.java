package com.example.mooc.repository;

import com.example.mooc.model.ReviewModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepository {

    ReviewModel create(@NotNull ReviewModel reviewModel);

    ReviewModel update(@NotNull ReviewModel reviewModel);

    Boolean delete(@NotNull ReviewModel reviewModel);

    Boolean deleteById(@NotNull Long id, @NotNull Long userId);

    ReviewModel findById(@NotNull Long id);

    List<ReviewModel> findAllByCourseId(@NotNull Long courseId, Pageable pageable);
}
