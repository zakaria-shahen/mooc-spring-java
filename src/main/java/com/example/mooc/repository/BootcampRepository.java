package com.example.mooc.repository;

import com.example.mooc.model.BootcampModel;
import com.example.mooc.repository.impl.interceptors.FilterBy;
import com.example.mooc.repository.impl.interceptors.Select;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BootcampRepository {

    BootcampModel create(BootcampModel bootcampModel);

    BootcampModel update(BootcampModel bootcampModel);

    Boolean delete(BootcampModel bootcampModel, boolean isAdmin);

    Boolean delete(Long id, Long userId, boolean isAdmin);

    List<BootcampModel> findAll(Pageable pageable, FilterBy filterBy);

    List<Map<String, Object>> findAll(Pageable pageable, FilterBy filterBy, Select select); // TODO

    BootcampModel findById(Long id);

    Boolean addPhoto(Long bootcampId, @NotBlank String filePath);

    Boolean deletePhoto(Long bootcampId, @NotBlank String filePath);

}
