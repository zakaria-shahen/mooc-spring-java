package com.example.mooc.repository;

import java.util.List;

public interface BootcampCareerRepository {

    Boolean createAll(List<Long> careerIds, Long bootcampId, Long userId, boolean isAdmin);

    Boolean deleteAll(List<Long> careerIds, Long bootcampId, Long userId, boolean isAdmin);

    Boolean create(Long careerId, Long bootcampId, Long userId, boolean isAdmin);

    Boolean delete(Long careerId, Long bootcampId, Long userId, boolean isAdmin);
}
