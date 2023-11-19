package com.example.mooc.repository;

import com.example.mooc.model.CareerModel;

import java.util.List;

public interface BootcampCareerRepository {

    Boolean createAll(List<CareerModel> careerModelList, Long bootcampId);

    Boolean deleteAll(List<CareerModel> careerModelList, Long bootcampId);

    Boolean create(CareerModel careerModel, Long bootcampId);

    Boolean delete(CareerModel careerModel, Long bootcampId);
}
