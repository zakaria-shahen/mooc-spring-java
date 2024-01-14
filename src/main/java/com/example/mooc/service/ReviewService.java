package com.example.mooc.service;

import com.example.mooc.dto.ReviewDto;
import com.example.mooc.mapping.ReviewModelToDtoMapper;
import com.example.mooc.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    public ReviewDto fetchById(Long reviewId) {
        var model = reviewRepository.findById(reviewId);
        return ReviewModelToDtoMapper.INSTANCE.toDto(model);
    }

    public List<ReviewDto> fetchAll(Long courseId, Pageable pageable) {
        var model = reviewRepository.findAllByCourseId(courseId, pageable);
        return ReviewModelToDtoMapper.INSTANCE.toDto(model);
    }

    public ReviewDto create(ReviewDto reviewDto) {
        var model = ReviewModelToDtoMapper.INSTANCE.toModel(reviewDto);
        model = reviewRepository.create(model);
        return ReviewModelToDtoMapper.INSTANCE.toDto(model);
    }

    public ReviewDto update(ReviewDto reviewDto) {
        var model = ReviewModelToDtoMapper.INSTANCE.toModel(reviewDto);
        model = reviewRepository.update(model);
        return ReviewModelToDtoMapper.INSTANCE.toDto(model);
    }
}
