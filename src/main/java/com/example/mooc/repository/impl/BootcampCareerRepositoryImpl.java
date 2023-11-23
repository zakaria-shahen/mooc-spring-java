package com.example.mooc.repository.impl;

import com.example.mooc.model.CareerModel;
import com.example.mooc.repository.BootcampCareerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
public class BootcampCareerRepositoryImpl implements BootcampCareerRepository {

    private final JdbcClient jdbcClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Boolean createAll(List<CareerModel> careerModelList, Long bootcampId) {
        var sql = STR."""
            DEFINE bootcamp_id_value = ?;
                \{careerModelList.stream().map(it ->
                    "insert into BOOTCAMP_CAREER(bootcamp_id, career_id) values(?, bootcamp_id_value);"
                    ).collect(Collectors.joining())
                }""".strip();
        var careerIds = careerModelList.stream().map(CareerModel::getId).toList();

        logger.info("trying to add careerList with size = {} to bootcamp id = {}", careerModelList.size(), bootcampId);
        return jdbcClient.sql(sql)
                .param(bootcampId)
                .params(careerIds)
                .update() == careerIds.size();
    }

    @Override
    public Boolean deleteAll(List<CareerModel> careerModelList, Long bootcampId) {
        var sql = STR."""
            DEFINE bootcamp_id_value = ?;
            \{careerModelList.stream().map(it ->
                "delete from BOOTCAMP_CAREER where bootcamp_id = bootcamp_id_value and career_id = ?"
            ).collect(Collectors.joining())}""".strip();
        var careerIds = careerModelList.stream().map(CareerModel::getId).toList();

        logger.info("trying to delete careerList with size = {} from bootcamp id = {}", careerModelList.size(), bootcampId);
        return jdbcClient.sql(sql)
                .param(bootcampId)
                .params(careerIds)
                .update() == careerIds.size();
    }

    @Override
    public Boolean create(CareerModel careerModel, Long bootcampId) {
        var sql = "insert into BOOTCAMP_CAREER(bootcamp_id, career_id) values(?, ?)";
        logger.info("trying to insert career {}, to bootcamp id = {}", careerModel, bootcampId);
        return jdbcClient.sql(sql)
                .param(bootcampId)
                .param(careerModel.getId())
                .update() == 1;
    }

    @Override
    public Boolean delete(CareerModel careerModel, Long bootcampId) {
        var sql = "delete from BOOTCAMP_CAREER where bootcamp_id = ? and career_id = ?";
        logger.info("trying to delete career = {}, to bootcamp id = {}", careerModel, bootcampId);
        return jdbcClient.sql(sql)
                       .param(bootcampId)
                       .param(careerModel.getId())
                       .update() == 1;
    }
}
