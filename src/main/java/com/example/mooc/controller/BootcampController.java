package com.example.mooc.controller;

import com.example.mooc.dto.response.BootcampDto;
import com.example.mooc.repository.impl.interceptors.FilterBy;
import com.example.mooc.service.BootcampService;
import com.example.mooc.utils.AuthorizationUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/bootcamp")
public class BootcampController {

    private BootcampService bootcampService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BootcampDto createBootcamp(@RequestBody BootcampDto bootcampDto, JwtAuthenticationToken principal) {
        bootcampDto.setUserId(AuthorizationUtils.getUserId(principal));
        return bootcampService.create(bootcampDto);
    }

    @PutMapping("/{id}")
    BootcampDto updateBootcamp(@PathVariable Long id, @RequestBody BootcampDto bootcampDto, JwtAuthenticationToken principal) {
        bootcampDto.setId(id);
        if (AuthorizationUtils.isNotAdmin(principal)) {
             bootcampDto.setUserId(AuthorizationUtils.getUserId(principal));
        }
        return bootcampService.updateBootcamp(bootcampDto);
    }

    @GetMapping
    List<BootcampDto> getAllBootcamp(Pageable pageable, @RequestParam(value = "filter-by", defaultValue = "") FilterBy filterBy) {
        return bootcampService.findAll(pageable, filterBy);
    }

    @GetMapping("/{id}")
    BootcampDto findOneById(@PathVariable Long id) {
        return bootcampService.findById(id);
    }

    @DeleteMapping("/{id}")
    void deleteBootcampById(@PathVariable Long id, JwtAuthenticationToken principal) {
        bootcampService.deleteById(id, AuthorizationUtils.getUserId(principal), AuthorizationUtils.isNotAdmin(principal));
    }


}