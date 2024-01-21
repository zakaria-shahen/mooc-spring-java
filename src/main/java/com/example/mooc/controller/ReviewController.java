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
@RequestMapping({"/bootcamp/{bootcampId}/course/{courseId}/review", "/bootcamp/course/{courseId}/review"})
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
         var userId = (reviewDto.getUserId() == null || AuthorizationUtils.isNotAdmin(principle))? AuthorizationUtils.getUserId(principle) : reviewDto.getUserId();
         reviewDto.setId(null);
         reviewDto.setCourseId(courseId);
         reviewDto.setUserId(userId);
         return reviewService.create(reviewDto);
     }

     @PutMapping("{reviewId}")
     public ReviewDto update(@PathVariable(required = false) Long bootcampId, @PathVariable Long courseId, @PathVariable Long reviewId, @RequestBody ReviewDto reviewDto, JwtAuthenticationToken principle) {
         var userId = (reviewDto.getUserId() == null || AuthorizationUtils.isNotAdmin(principle))? AuthorizationUtils.getUserId(principle) : reviewDto.getUserId();
         reviewDto.setUserId(userId);
         if (reviewDto.getId() == null) {
            reviewDto.setId(reviewId);
         }
         if (reviewDto.getCourseId() == null) {
             reviewDto.setCourseId(courseId);
         }
         return reviewService.update(reviewDto);
     }

}
