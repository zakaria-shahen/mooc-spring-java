package com.example.mooc.controller;

import com.example.mooc.model.CareerModel;
import com.example.mooc.service.CareerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/career")
public class CareerController {
    private CareerService careerService;

    @GetMapping
    public List<CareerModel> fetchAll(Pageable pageable) {
        return careerService.fetchAll(pageable);
    }

    @PostMapping
    public CareerModel create(@RequestBody CareerModel careerModel) {
        return careerService.create(careerModel);

    }
}
