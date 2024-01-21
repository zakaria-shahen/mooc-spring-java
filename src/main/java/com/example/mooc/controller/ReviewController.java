package com.example.mooc.controller;

import com.example.mooc.dto.ReviewDto;
import com.example.mooc.service.ReviewService;
import com.example.mooc.utils.AuthorizationUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/bootcamp/{bootcampId}/course/{courseId}/review")
public class ReviewController {

    private ReviewService reviewService;

     @GetMapping("{reviewId}")
     public ReviewDto fetchOne(@PathVariable(required = false) Long bootcampId, @PathVariable(required = false) Long courseId, @PathVariable Long reviewId) {
          return reviewService.fetchById(reviewId);
     }

     @GetMapping
     public List<ReviewDto> fetchAll(@PathVariable(required = false) Long bootcampId, @PathVariable Long courseId, Pageable pageable) {
         return reviewService.fetchAll(courseId, pageable);
     }

     @PostMapping
     public ReviewDto create(@PathVariable(required = false) Long bootcampId, @PathVariable Long courseId, @RequestBody ReviewDto reviewDto, JwtAuthenticationToken principle) {
         reviewDto.setId(null);
         reviewDto.setCourseId(courseId);
         reviewDto.setUserId(AuthorizationUtils.getUserId(principle));
         return reviewService.create(reviewDto);
     }

     @PutMapping("{reviewId}")
     public ReviewDto update(@PathVariable(required = false) Long bootcampId, @PathVariable Long courseId, @PathVariable Long reviewId, @RequestBody ReviewDto reviewDto, JwtAuthenticationToken principle) {
         reviewDto.setId(reviewId);
         reviewDto.setCourseId(courseId);
         reviewDto.setUserId(AuthorizationUtils.getUserId(principle));
         return reviewService.update(reviewDto);
     }

}
