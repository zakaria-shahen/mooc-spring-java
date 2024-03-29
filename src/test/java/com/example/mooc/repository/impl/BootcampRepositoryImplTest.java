package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileDeletingException;
import com.example.mooc.exception.NotFoundResourceWhileFetchingException;
import com.example.mooc.exception.NotFoundResourceWhileUpdatingException;
import com.example.mooc.model.BootcampModel;
import com.example.mooc.repository.BootcampRepository;
import com.example.mooc.repository.impl.interceptors.FilterBy;
import com.example.mooc.repository.impl.interceptors.Select;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SpringBootTest
@Testcontainers(parallel = true)
class BootcampRepositoryImplTest {

    @Autowired
    private BootcampRepository bootcampRepository;
    private final Supplier<BootcampModel> bootcampSupplier = () -> BootcampModel.builder()
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
        @DisplayName("When trying to delete bootcamp doesn't exists Then throw exception")
        void deleteWithNotFoundResourceWhileDeletingException() {
            Assertions.assertThatExceptionOfType(NotFoundResourceWhileDeletingException.class)
                    .isThrownBy(() -> bootcampRepository.delete(1000L, 1L, true));
        }

        @Test
        @DisplayName("When trying to delete bootcamp exists Then returns true")
        void deleteSuccessfully() {
            var persistenceBootcamp = bootcampRepository.create(bootcampSupplier.get());
            Assertions.assertThat(bootcampRepository.delete(persistenceBootcamp, true)).isTrue();
        }
    }


    @Nested
    class UpdateTests {
        @Test
        @DisplayName("when trying to updating bootcamp doesn't exists then throw exception")
        void throwException() {
            var updateBootcamp = bootcampSupplier.get();
            updateBootcamp.setAddress("cairo");
            Assertions.assertThatExceptionOfType(NotFoundResourceWhileUpdatingException.class)
                    .isThrownBy(() -> bootcampRepository.update(updateBootcamp));
        }

        @Test
        @DisplayName("return updated bootcamp when call update Bootcamp method")
        void successfully() {
            var persistenceBootcamp = bootcampRepository.create(bootcampSupplier.get());
            persistenceBootcamp.setAddress("cairo");
            var returnsBootcamp = bootcampRepository.update(persistenceBootcamp);
            persistenceBootcamp = bootcampRepository.findById(persistenceBootcamp.getId());

            Assertions.assertThatList(List.of(returnsBootcamp.getAddress(), persistenceBootcamp.getAddress()))
                    .allMatch(it -> it.equals("cairo"));

        }

    }

    @Nested
    class CreateTests {
        @Test
        @DisplayName("when create bootcamp successfully then returns created Bootcamp have id value")
        void successfully() {
            var persistenceBootcamp = bootcampRepository.create(bootcampSupplier.get());
            Assertions.assertThat(persistenceBootcamp.getId()).isNotNull();
        }
    }

    @Nested
    class findTests {
        @Test
        @DisplayName("when find by id and ID is exists then returns one bootcamp")
        void fineByIdSuccessfully() {
            var persistenceBootcamp = bootcampRepository.create(bootcampSupplier.get());
            persistenceBootcamp = bootcampRepository.findById(persistenceBootcamp.getId());
            Assertions.assertThat(persistenceBootcamp).isNotNull();
        }

        @Test
        @DisplayName("failWhenFindByIdNotExists")
        void failWhenFindByIdNotExists() {
            Assertions.assertThatExceptionOfType(NotFoundResourceWhileFetchingException.class)
                    .isThrownBy(() -> bootcampRepository.findById(1000000000L));
        }

        @Test
        @DisplayName("when trying to find all bootcamp should returns list of bootcamp")
        void findAllSuccessfully() {
             bootcampRepository.create(bootcampSupplier.get());
             var bootcampList = bootcampRepository.findAll(Pageable.ofSize(10), new FilterBy(List.of(), List.of()));
             Assertions.assertThat(bootcampList)
                     .isNotEmpty()
                     .hasAtLeastOneElementOfType(BootcampModel.class);
        }

        @Test
        @DisplayName("when trying to find all bootcamp and select columns then returns list of bootcamp")
        void findAllSuccessfullyWithSelectColumns() {
            bootcampRepository.create(bootcampSupplier.get());
            var bootcampList = bootcampRepository.findAll(Pageable.ofSize(10), new FilterBy(List.of(), List.of()), new Select("id"));
            Assertions.assertThat(bootcampList)
                    .isNotEmpty()
                    .hasAtLeastOneElementOfType(Map.class);
            Assertions.assertThat(bootcampList.getFirst().get("id")).isNotNull();
        }

        @Test
        @DisplayName("when trying to find all bootcamp and DB doesn't have Bootcamp should returns empty list")
        void findAllSuccessfullyWithEmptyList() {
            bootcampRepository.create(bootcampSupplier.get());
            var bootcampList = bootcampRepository.findAll(Pageable.ofSize(10), new FilterBy(List.of("name"), List.of("omar_")));

            Assertions.assertThat(bootcampList)
                    .isEmpty();
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Photo {
        private final String PHOTO_PATH = "photo";
        private final Long bootcampId = bootcampRepository.create(bootcampSupplier.get()).getId();

        @Test
        @DisplayName("when trying to add photo to bootcamp then returns true")
        void create() {
             var status = bootcampRepository.addPhoto(bootcampId, PHOTO_PATH);
             Assertions.assertThat(status).isTrue();
        }

        @Test
        @DisplayName("when trying to delete photo from bootcamp then returns true")
        void delete() {
            var status = bootcampRepository.deletePhoto(bootcampId, PHOTO_PATH);
            Assertions.assertThat(status).isTrue();
        }

    }

}