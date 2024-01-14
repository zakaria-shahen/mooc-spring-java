package com.example.mooc.controller;

import com.example.mooc.dto.request.BootcampCareerDto;
import com.example.mooc.service.BootcampLinkWithService;
import com.example.mooc.utils.AuthorizationUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/bootcamp/{bootcampId}")
public class BootcampLinkWithController {

    private BootcampLinkWithService bootcampLinkWithService;

    @PostMapping("/career/{careerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void linkCareer(@PathVariable Long bootcampId, @PathVariable Long careerId, JwtAuthenticationToken principal) {
        Long userId = AuthorizationUtils.getUserId(principal);
        bootcampLinkWithService.linkCareer(careerId, bootcampId, userId, AuthorizationUtils.isAdmin(principal));
    }

    @PostMapping("/career")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void linkCareers(@PathVariable Long bootcampId, @RequestBody BootcampCareerDto careerIds, JwtAuthenticationToken principal) {
        Long userId = AuthorizationUtils.getUserId(principal);
        bootcampLinkWithService.linkCareers(careerIds.careerIds(), bootcampId, userId, AuthorizationUtils.isAdmin(principal));
    }

    @DeleteMapping("/career/{careerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkCareer(@PathVariable Long bootcampId, @PathVariable Long careerId, JwtAuthenticationToken principal) {
        Long userId = AuthorizationUtils.getUserId(principal);
        bootcampLinkWithService.unlinkCareer(careerId, bootcampId, userId, AuthorizationUtils.isAdmin(principal));
    }

    @DeleteMapping("/career")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkCareers(@PathVariable Long bootcampId, @RequestBody BootcampCareerDto careerIds, JwtAuthenticationToken principal) {
        Long userId = AuthorizationUtils.getUserId(principal);
        bootcampLinkWithService.unlinkCareers(careerIds.careerIds(), bootcampId, userId, AuthorizationUtils.isAdmin(principal));
    }


}