package com.example.mooc.repository.impl;

import com.example.mooc.model.CareerModel;
import com.example.mooc.repository.CareerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class CareerRepositoryImpl implements CareerRepository {

    private final JdbcClient jdbcClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<CareerModel> findAll() {
        logger.info("fetch all career");
        return jdbcClient
                .sql("select id, name from CAREER")
                .query(CareerModel.class)
                .list();
    }
}
