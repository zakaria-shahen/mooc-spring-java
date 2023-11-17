package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileDeletingException;
import com.example.mooc.exception.NotFoundResourceWhileFetchingException;
import com.example.mooc.exception.NotFoundResourceWhileUpdatingException;
import com.example.mooc.exception.SomethingWantWrongWhileFetchingIdException;
import com.example.mooc.model.BootcampModel;
import com.example.mooc.repository.BootcampRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.SimplePropertySqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BootcampRepositoryImpl implements BootcampRepository {

    private final Logger logger = LoggerFactory.getLogger(BootcampRepositoryImpl.class);
    private final JdbcClient jdbcClient;

    @Override
    public BootcampModel create(@NonNull BootcampModel bootcampModel) {
        String sql = """
            insert into BOOTCAMP(name, description, website, phone, email, address, housing, job_assistance, job_guarantee, average_cost, average_rating, user_id)
            values(:name, :description, :website, :phone, :email, :address, :housing, :jobAssistance, :jobGuarantee, :averageCost, :averageRating, :userId)
        """.strip();
        logger.info("trying execute insert query against BOOTCAMP for bootcamp name -> {}", bootcampModel.getName());
        logger.debug("execute insert query: {}", sql);
        var keyHolder = new GeneratedKeyHolder();
        // `jdbcClient.update(keyHolder, ... keyColumnNames) method
        // it's exists because my issue & PR 😎: https://github.com/spring-projects/spring-framework/issues/31607
        jdbcClient.sql(sql)
                .paramSource(bootcampModel)
                .update(keyHolder, "id");

        var id = keyHolder.getKey();
        logger.debug("my Error:  {}", id);
        if (id == null) {
            logger.debug("Fail, unable to get resource ID!, keyHolder value is NULL");
            throw new SomethingWantWrongWhileFetchingIdException();
        }
        bootcampModel.setId(id.longValue());
        return bootcampModel;
    }

    @Override
    public BootcampModel update(@NonNull BootcampModel bootcampModel) {
        var sql = """
            update BOOTCAMP SET 
                name = :name, 
                description = :description,
                website = :website,
                phone = :phone,
                email = :email,
                address = :address,
                housing = :housing,
                job_assistance = :jobAssistance,
                job_guarantee = :jobGuarantee,
                average_cost = :averageCost,
                average_rating = :averageRating,
                user_id = :userId 
            where id = :id
        """.strip();
        logger.info("trying updating query against BOOTCAMP for bootcamp id -> {}", bootcampModel.getId());
        logger.debug("execute update query: {}", sql);
        var affectRows = jdbcClient.sql(sql)
                .param(bootcampModel)
                .update();
        if (affectRows != 1) {
            logger.error("Fail, while updating BOOTCAMP ID={}, affectRow={}", bootcampModel.getId(), affectRows);
            throw new NotFoundResourceWhileUpdatingException();
        }
        logger.info("1 affect row, BOOTCAMP ID={}", bootcampModel.getId());
        return bootcampModel;
    }

    @Override
    public Boolean delete(@NonNull BootcampModel bootcampModel) {
        return delete(bootcampModel.getId());
    }

    @Override
    public Boolean delete(@NonNull Long id) {
        var sql = "delete from BOOTCAMP where id = ?";
        var affectRows = jdbcClient.sql(sql)
                .param(id)
                .update();
        if (affectRows != 1) {
            logger.debug("Fail, while deleting BOOTCAMP ID={}, affectRows={}", id, affectRows);
            throw new NotFoundResourceWhileDeletingException();
        }
        logger.info("Successfully deleting BOOTCAMP, ID={}", id);
        return true;
    }

    @Override
    public List<BootcampModel> findAll() {
        var sql = """
        select 
            id, description, website, phone, email, address, housing, job_assistance, job_guarantee, average_cost, average_rating, user_id 
        from BOOTCAMP
        """.strip();
        logger.info("trying to fetch all BOOTCAMP");
        logger.debug("execute select query: {}", sql);
        return jdbcClient.sql(sql)
                .query(BootcampModel.class)
                .list();
    }

    @Override
    public BootcampModel findById(@NonNull Long id) {
        var sql = """
        select 
            id, description, website, phone, email, address, housing, job_assistance, job_guarantee, average_cost, average_rating, user_id 
        from BOOTCAMP where id = ?
        """.strip();
        logger.info("trying to fetch one BOOTCAMP where id = {}", id);
        logger.debug("execute select query: {}", sql);
        return jdbcClient.sql(sql)
                .param(id)
                .query(BootcampModel.class)
                .optional()
                .orElseThrow(NotFoundResourceWhileFetchingException::new);
    }
}
