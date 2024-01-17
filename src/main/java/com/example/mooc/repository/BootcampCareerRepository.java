package com.example.mooc.repository;

import com.example.mooc.model.CareerModel;

import java.util.List;

public interface BootcampCareerRepository {

    Boolean createAll(List<Long> careerIds, Long bootcampId, Long userId, boolean isAdmin);

    Boolean deleteAll(List<Long> careerIds, Long bootcampId, Long userId, boolean isAdmin);

    Boolean create(Long careerId, Long bootcampId, Long userId, boolean isAdmin);

    Boolean delete(Long careerId, Long bootcampId, Long userId, boolean isAdmin);

    List<CareerModel> findAllByBootcampId(Long id);
}
