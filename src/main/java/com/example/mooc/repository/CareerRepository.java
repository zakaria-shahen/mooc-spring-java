package com.example.mooc.repository;

import com.example.mooc.model.CareerModel;

import java.util.List;

public interface CareerRepository {
    List<CareerModel> findAll();
}
