package com.example.mooc.service;

import com.example.mooc.repository.BootcampCareerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BootcampLinkWithService {
    private BootcampCareerRepository bootcampCareerRepository;

    public Boolean linkCareers(List<Long> careerIds, Long bootcampId, Long userId, Boolean isAdmin) {
        return bootcampCareerRepository.createAll(careerIds, bootcampId, userId, isAdmin);
    }

    public Boolean unlinkCareers(List<Long> careerIds, Long bootcampId, Long userId, Boolean isAdmin) {
        return bootcampCareerRepository.deleteAll(careerIds, bootcampId, userId, isAdmin);
    }

    public Boolean linkCareer(Long careerModel, Long bootcampId, Long userId, Boolean isAdmin) {
        return bootcampCareerRepository.create(careerModel, bootcampId, userId, isAdmin);
    }

    public Boolean unlinkCareer(Long careerModel, Long bootcampId, Long userId, Boolean isAdmin) {
        return bootcampCareerRepository.delete(careerModel, bootcampId, userId, isAdmin);
    }

}
