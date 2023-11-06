package com.example.mooc.repository;

import com.example.mooc.model.BootcampModel;

import java.util.List;

public interface BootcampRepository {

    BootcampModel create(BootcampModel bootcampModel);

    BootcampModel update(BootcampModel bootcampModel);

    Boolean delete(BootcampModel bootcampModel);

    Boolean delete(Long id);

    List<BootcampModel> findAll();

    BootcampModel findById(Long id);
}
