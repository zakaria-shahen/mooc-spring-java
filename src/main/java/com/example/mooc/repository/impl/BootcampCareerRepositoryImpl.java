package com.example.mooc.repository.impl;

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
    public Boolean createAll(List<Long> careerIds, Long bootcampId, Long userId, boolean isAdmin) {
        var sql = STR."""
             declare
                bootcamp_id_value number;
                have_access number;
             begin
                select ? into bootcamp_id_value from dual;
                have_access := check_user_onw_bootcamp(bootcamp_id_value, ?, ?);
                \{careerIds.stream().map (it ->
                        "insert into BOOTCAMP_CAREER(bootcamp_id, career_id) values(bootcamp_id_value, ?);"
                    ).collect(Collectors.joining())
                }
             end;""".strip();

        logger.info("trying to add careerList with size = {} to bootcamp id = {}", careerIds.size(), bootcampId);
        return jdbcClient.sql(sql)
                .param(bootcampId)
                .param(userId)
                .param(isAdmin ? 1 : 0)
                .params(careerIds)
                .update() == 1;
    }

    @Override
    public Boolean deleteAll(List<Long> careerIds, Long bootcampId, Long userId, boolean isAdmin) {
        var sql = STR."""
             declare
                bootcamp_id_value number;
                have_access number;
             begin
                select ? into bootcamp_id_value from dual;
                have_access := check_user_onw_bootcamp(bootcamp_id_value, ?, ?);
                \{careerIds.stream().map(it ->
                        "delete from BOOTCAMP_CAREER where bootcamp_id = bootcamp_id_value and career_id = ?;"
                ).collect(Collectors.joining())}
            end;""".strip();

        logger.info("trying to delete careerList with size = {} from bootcamp id = {}", careerIds.size(), bootcampId);
        return jdbcClient.sql(sql)
                .param(bootcampId)
                .param(userId)
                .param(isAdmin ? 1 : 0)
                .params(careerIds)
                .update() == 1;
    }

    @Override
    public Boolean create(Long careerId, Long bootcampId, Long userId, boolean isAdmin) {
        var sql = """
             declare
                have_access number;
             begin
                have_access := check_user_onw_bootcamp(:bootcampId, :userId, :isAdmin);
                insert into BOOTCAMP_CAREER(bootcamp_id, career_id) values(:bootcampId, :careerId);
            end;""".strip();
        logger.info("trying to insert career {}, to bootcamp id = {}", careerId, bootcampId);
        return jdbcClient.sql(sql)
                .param("bootcampId", bootcampId)
                .param("userId", userId)
                .param("isAdmin", isAdmin? 1 : 0)
                .param("careerId", careerId)
                .update() == 1;
    }

    @Override
    public Boolean delete(Long careerId, Long bootcampId, Long userId, boolean isAdmin) {
        var sql = "delete from BOOTCAMP_CAREER where #bootcamp_id = @ and #career_id = @ and check_user_onw_bootcamp(:bootcampId, :userId, :isAdmin) = 1";
        logger.info("trying to delete career = {}, to bootcamp id = {}", careerId, bootcampId);
        return jdbcClient.sql(sql)
                       .param("bootcampId", bootcampId)
                       .param("careerId", careerId)
                       .param("userId", userId)
                       .param("isAdmin", isAdmin? 1 : 0)
                       .update() == 1;
    }
}
