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
@RequestMapping({"/bootcamp/{bootcampId}/course", "/bootcamp/course"})
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
        var isNotAdmin = AuthorizationUtils.isNotAdmin(principle);
        var userId = (courseDto.getUserId() == null || isNotAdmin) ? AuthorizationUtils.getUserId(principle) : courseDto.getUserId();
        courseDto.setUserId(userId);
        courseDto.setBootcampId(bootcampId);
        courseDto.setId(null);
        return courseService.create(courseDto, !isNotAdmin);
    }

    @PutMapping("/{courseId}")
    public CourseDto update(@PathVariable(required = false) Long bootcampId, @PathVariable(required = false) Long courseId, @RequestBody CourseDto courseDto, JwtAuthenticationToken principle) {
        var isNotAdmin = AuthorizationUtils.isNotAdmin(principle);
        var userId = (courseDto.getUserId() == null || isNotAdmin) ? AuthorizationUtils.getUserId(principle) : courseDto.getUserId();
        courseDto.setUserId(userId);
        if (courseId != null) {
            courseDto.setId(courseId);
        }
        if (bootcampId != null) {
            courseDto.setBootcampId(bootcampId) ;
        }
        return courseService.update(courseDto, !isNotAdmin);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(required = false) Long bootcampId, @PathVariable Long courseId, JwtAuthenticationToken principle) {
         courseService.delete(courseId, AuthorizationUtils.getUserId(principle), AuthorizationUtils.isAdmin(principle));
    }


}
