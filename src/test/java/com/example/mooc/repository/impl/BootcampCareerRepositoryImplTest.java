package com.example.mooc.repository.impl;

import com.example.mooc.exception.ResourceYouTryToLinkToIsAlreadyLinked;
import com.example.mooc.model.BootcampModel;
import com.example.mooc.model.CareerModel;
import com.example.mooc.repository.BootcampCareerRepository;
import com.example.mooc.repository.BootcampRepository;
import com.example.mooc.repository.CareerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;


@SpringBootTest
@Testcontainers(parallel = true)
class BootcampCareerRepositoryImplTest {

    @Autowired
    private BootcampCareerRepository bootcampCareerRepository;
    @Autowired
    private CareerRepository careerRepository;
    private CareerModel backendCareer = new CareerModel(null, STR."back-end:\{UUID.randomUUID()}");
    private CareerModel devOpsCareer = new CareerModel(null, STR."devops:\{UUID.randomUUID()}");
    private CareerModel mobileCareer = new CareerModel(null, STR."mobile:\{UUID.randomUUID()}");
    private CareerModel frontEndCareer = new CareerModel(null, STR."front-end:\{UUID.randomUUID()}");
    private Long bootcampId = null;
    @Autowired
    BootcampRepository bootcampRepository;

    @BeforeEach
    void init() {
        backendCareer = careerRepository.create(backendCareer);
        devOpsCareer = careerRepository.create(devOpsCareer);
        mobileCareer = careerRepository.create(mobileCareer);
        frontEndCareer = careerRepository.create(frontEndCareer);
        bootcampId = bootcampRepository.create(BootcampModel.builder()
                .name("omar")
                .description("any")
                .website("any")
                .phone("any")
                .email("any")
                .address("any")
                .housing(true)
                .jobGuarantee(true)
                .jobAssistance(true)
                .userId(1L)
                .build()).getId();
    }

    @Nested
    class Create {
        @Test
        @DisplayName("when trying to add list of career to bootcamp should returns true")
        void createByList() {
            Assertions.assertThat(bootcampId).isNotNull();
            var status = bootcampCareerRepository.createAll(List.of(backendCareer, devOpsCareer), bootcampId);
            Assertions.assertThat(status).isTrue();
            status = bootcampCareerRepository.createAll(List.of(mobileCareer), bootcampId);
            Assertions.assertThat(status).isTrue();
        }

        @Test
        @DisplayName("when trying to add one career to bootcamp then returns true")
        void createByOne() {

            var status = bootcampCareerRepository.create(frontEndCareer, bootcampId);
            Assertions.assertThat(status).isTrue();
        }

        @Test
        @DisplayName("when trying to add career to bootcamp and this career exists in bootcamp then throw exception")
        @Disabled("TODO: move next test case to service layer test cases")
        void throwsWhenItExists() {
            bootcampCareerRepository.create(frontEndCareer, bootcampId);
            Assertions.assertThatExceptionOfType(ResourceYouTryToLinkToIsAlreadyLinked.class)
                    .isThrownBy(() -> bootcampCareerRepository.create(frontEndCareer, bootcampId));
        }
    }

    @Nested
    class Delete {
        @Test
        @DisplayName("when trying to delete list of career exists in bootcamp then returns true")
        void deleteByList() {
            var careerList = List.of(backendCareer, devOpsCareer);
            bootcampCareerRepository.createAll(careerList, bootcampId);
            var status = bootcampCareerRepository.deleteAll(careerList, bootcampId);
            Assertions.assertThat(status).isTrue();
        }


        @Test
        @DisplayName("when trying to delete one career exists in bootcamp then returns true")
        void deleteOne() {
            bootcampCareerRepository.create(backendCareer, bootcampId);
            var status = bootcampCareerRepository.delete(backendCareer, bootcampId);
            Assertions.assertThat(status).isTrue();
        }
    }
}