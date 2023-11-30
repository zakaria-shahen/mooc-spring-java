package com.example.mooc.repository.impl;

import com.example.mooc.controller.HealthController;
import com.example.mooc.exception.ResourceYouTryToLinkToIsAlreadyLinked;
import com.example.mooc.model.CareerModel;
import com.example.mooc.repository.CareerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;
import java.util.function.Supplier;

@SpringBootTest
@Testcontainers(parallel = true)
@ActiveProfiles("dev")
class CareerRepositoryImplTest {

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    HealthController healthController;

    private final Supplier<CareerModel> careerModelSupplier = () -> new CareerModel(null, STR."java:\{UUID.randomUUID()}");

    @Test
    @DisplayName("when fetch career then returns list of career")
    void fetchAll() {
        careerRepository.create(careerModelSupplier.get());
        var careerList = careerRepository.findAll(Pageable.ofSize(2));
        Assertions.assertThat(careerList).isNotNull().hasSizeGreaterThan(1);
    }

    @Test
    @DisplayName("when trying to create one career then returns career with id.")
    void createOne() {
        var career = careerRepository.create(careerModelSupplier.get());
        Assertions.assertThat(career).isNotNull();
        Assertions.assertThat(career.getId()).isNotNull();
    }

    // TODO: move next test case to service layer test cases.
    @Test
    @DisplayName("when trying to create one career then returns career with id.")
    void createOneWithThrow() {
        var career = careerRepository.create(careerModelSupplier.get());
        Assertions.assertThatExceptionOfType(ResourceYouTryToLinkToIsAlreadyLinked.class)
                .isThrownBy(() -> careerRepository.create(career));
    }


}