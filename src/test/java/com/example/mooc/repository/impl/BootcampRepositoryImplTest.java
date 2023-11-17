package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileDeletingException;
import com.example.mooc.model.BootcampModel;
import com.example.mooc.repository.BootcampRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("dev")
class BootcampRepositoryImplTest {

    @Autowired
    private BootcampRepository bootcampRepository;
    private final BootcampModel dumpBootcamp = BootcampModel.builder()
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
            .build();

    @Nested
    class DeleteTests {
        @Test
        @DisplayName("When trying to delete bootcamp doesn't exits Then throw exception")
        void deleteWithNotFoundResourceWhileDeletingException() {
            Assertions.assertThatExceptionOfType(NotFoundResourceWhileDeletingException.class)
                    .isThrownBy(() -> bootcampRepository.delete(1L));
        }

        @Test
        @DisplayName("When trying to delete bootcamp exits Then returns true")
        void deleteSuccessfully() {
            bootcampRepository.create(dumpBootcamp);
            Assertions.assertThat(bootcampRepository.delete(1L)).isTrue();
        }
    }
}