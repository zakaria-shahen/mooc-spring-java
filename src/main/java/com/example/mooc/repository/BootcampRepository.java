package com.example.mooc.repository;

import com.example.mooc.model.BootcampModel;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface BootcampRepository {

    BootcampModel create(BootcampModel bootcampModel);

    BootcampModel update(BootcampModel bootcampModel);

    Boolean delete(BootcampModel bootcampModel);

    Boolean delete(Long id);

    List<BootcampModel> findAll();

    BootcampModel findById(Long id);

    Boolean addPhoto(Long bootcampId, @NotBlank String filePath);

    Boolean deletePhoto(Long bootcampId, @NotBlank String filePath);

}
