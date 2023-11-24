package com.example.mooc.repository;

import com.example.mooc.model.CareerModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CareerRepository {
    List<CareerModel> findAll(Pageable pageable);
}
