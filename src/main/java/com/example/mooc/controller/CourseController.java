package com.example.mooc.controller;

import com.example.mooc.dto.CourseDto;
import com.example.mooc.service.CourseService;
import com.example.mooc.utils.AuthorizationUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/bootcamp/{bootcampId}/course")
public class CourseController {

    private CourseService courseService;

    @GetMapping("/{courseId}")
    public CourseDto fetchOne(@PathVariable(required = false) Long bootcampId, @PathVariable Long courseId) {
        return courseService.fetchById(courseId);
    }

    @GetMapping
    public List<CourseDto> fetchAll(@PathVariable Long bootcampId, Pageable pageable) {
        return courseService.fetchAll(bootcampId, pageable);
    }

    @PostMapping
    public CourseDto create(@RequestBody CourseDto courseDto, @PathVariable Long bootcampId, JwtAuthenticationToken principle) {
        courseDto.setId(null);
        courseDto.setBootcampId(bootcampId);
        courseDto.setUserId(AuthorizationUtils.getUserId(principle));
        return courseService.create(courseDto, AuthorizationUtils.isAdmin(principle));
    }

    @PostMapping("/{courseId}")
    public CourseDto update(@PathVariable Long bootcampId, @PathVariable Long courseId, @RequestBody CourseDto courseDto, JwtAuthenticationToken principle) {
        courseDto.setId(courseId);
        courseDto.setBootcampId(bootcampId);
        courseDto.setUserId(AuthorizationUtils.getUserId(principle));
        return courseService.update(courseDto, AuthorizationUtils.isAdmin(principle));
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long bootcampId, @PathVariable Long courseId, JwtAuthenticationToken principle) {
         courseService.delete(courseId, AuthorizationUtils.getUserId(principle), AuthorizationUtils.isAdmin(principle));
    }


}
