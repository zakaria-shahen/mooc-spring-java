package com.example.mooc.repository.impl;

import com.example.mooc.exception.SomethingWantWrongWhileFetchingIdException;
import com.example.mooc.model.CareerModel;
import com.example.mooc.repository.CareerRepository;
import com.example.mooc.repository.impl.interceptors.specification.CustomJdbcClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class CareerRepositoryImpl implements CareerRepository {

    private final CustomJdbcClient jdbcClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<CareerModel> findAll(Pageable pageable) {
        logger.info("fetch all career");
        return jdbcClient.pageable(pageable)
                .sql("select id, name from CAREER")
                .query(CareerModel.class)
                .list();
    }

    @Override
    public CareerModel create(CareerModel careerModel) {
        String sql = "insert into CAREER(#name) values(@@)";
        logger.info("trying execute insert query against CAREER for career name -> {}", careerModel.getName());
        logger.debug("execute insert query: {}", sql);
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .paramSource(careerModel)
                .update(keyHolder, "id");

        var id = keyHolder.getKey();
        if (id == null) {
            logger.debug("Fail, unable to get resource ID!, keyHolder value is NULL");
            throw new SomethingWantWrongWhileFetchingIdException();
        }
        careerModel.setId(id.longValue());
        return careerModel;
    }
}
