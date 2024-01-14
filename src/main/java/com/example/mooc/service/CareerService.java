package com.example.mooc.service;

import com.example.mooc.model.CareerModel;
import com.example.mooc.repository.CareerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CareerService {

    private CareerRepository careerRepository;

    public List<CareerModel> fetchAll(Pageable pageable) {
        return careerRepository.findAll(pageable);
    }

    public CareerModel create(CareerModel careerModel) {
        return careerRepository.create(careerModel);
    }

}
