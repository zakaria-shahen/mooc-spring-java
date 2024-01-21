package com.example.mooc.controller;

import com.example.mooc.dto.BootcampDto;
import com.example.mooc.dto.response.BootcampFullDto;
import com.example.mooc.repository.impl.interceptors.FilterBy;
import com.example.mooc.repository.impl.interceptors.Select;
import com.example.mooc.service.BootcampService;
import com.example.mooc.utils.AuthorizationUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/bootcamp")
public class BootcampController {

    private BootcampService bootcampService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BootcampDto createBootcamp(@RequestBody BootcampDto bootcampDto, JwtAuthenticationToken principal) {
        var userId = (bootcampDto.getUserId() == null || AuthorizationUtils.isNotAdmin(principal)) ? AuthorizationUtils.getUserId(principal) : bootcampDto.getUserId();
        bootcampDto.setUserId(userId);
        bootcampDto.setAverageCost(null);
        bootcampDto.setAverageRating(null);
        return bootcampService.create(bootcampDto);
    }

    @PutMapping("/{id}")
    public BootcampDto updateBootcamp(@PathVariable Long id, @RequestBody BootcampDto bootcampDto, JwtAuthenticationToken principal) {
        var userId = (bootcampDto.getUserId() == null || AuthorizationUtils.isNotAdmin(principal))? AuthorizationUtils.getUserId(principal) : id;
        bootcampDto.setUserId(userId);
        bootcampDto.setAverageCost(null);
        bootcampDto.setAverageRating(null);
        bootcampDto.setId(id);
        return bootcampService.updateBootcamp(bootcampDto);
    }

    @GetMapping
    public List<Map<String, Object>> getAllBootcamp(
            Pageable pageable,
            @RequestParam(value = "filter-by", defaultValue = "") FilterBy filterBy,
            @RequestParam(defaultValue = "") Select select
    ) {
        return bootcampService.findAll(pageable, filterBy, select);
    }

    @GetMapping("/{id}")
    public BootcampFullDto findOneByIdWithFullInfo(@PathVariable Long id) {
        return bootcampService.findByIdWithFullInfo(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBootcampById(@PathVariable Long id, JwtAuthenticationToken principal) {
        bootcampService.deleteById(id, AuthorizationUtils.getUserId(principal), AuthorizationUtils.isAdmin(principal));
    }


}
