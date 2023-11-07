package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileDeletingException;
import com.example.mooc.exception.NotFoundResourceWhileFetchingException;
import com.example.mooc.exception.NotFoundResourceWhileUpdatingException;
import com.example.mooc.exception.SomethingWantWrongWhileFetchingIdException;
import com.example.mooc.model.BootcampModel;
import com.example.mooc.repository.BootcampRepository;
import com.example.mooc.utils.SQLQueryBuilderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BootcampRepositoryImpl implements BootcampRepository {

    private final Logger logger = LoggerFactory.getLogger(BootcampRepositoryImpl.class);
    private final JdbcClient jdbcClient;
    private final SQLQueryBuilderUtils bootcampModelSqlUtils = BootcampModel.sqlModuleUtils;

    public BootcampRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public BootcampModel create(@NonNull BootcampModel bootcampModel) {
        String sql = STR. """
            insert into BOOTCAMP(\{ bootcampModelSqlUtils.parameterNames })
            values(\{ bootcampModelSqlUtils.placeholderParameters })
        """ .trim();
        logger.info("trying execute insert query against BOOTCAMP for bootcamp name -> {}", bootcampModel.getName());
        logger.debug("execute insert query: {}", sql);
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .param(bootcampModel)
                .update(keyHolder);
        var id = keyHolder.getKey();
        if (id == null) {
            logger.debug("Fail, unable to get resource ID!, keyHolder value is NULL");
            throw new SomethingWantWrongWhileFetchingIdException();
        }
        bootcampModel.setId(id.longValue());
        return bootcampModel;
    }

    @Override
    public BootcampModel update(@NonNull BootcampModel bootcampModel) {
        var sql = STR. "update BOOTCAMP SET \{ bootcampModelSqlUtils.fieldsNamesForUpdate } where id = ?" ;
        logger.info("trying updating query against BOOTCAMP for bootcamp id -> {}", bootcampModel.getId());
        logger.debug("execute update query: {}", sql);
        var affectRows = jdbcClient.sql(sql)
                .param(bootcampModel)
                .param(bootcampModel.getName())
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
        var sql = STR."delete BOOTCAMP where id = ?";
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
        var sql = STR. "select id, \{ bootcampModelSqlUtils.parameterNames } from BOOTCAMP" ;
        logger.info("trying to fetch all BOOTCAMP");
        logger.debug("execute select query: {}", sql);
        return jdbcClient.sql(sql)
                .query(BootcampModel.class)
                .list();
    }

    @Override
    public BootcampModel findById(@NonNull Long id) {
        var sql = STR. "select id, \{ bootcampModelSqlUtils.parameterNames } from BOOTCAMP" ;
        logger.info("trying to fetch one BOOTCAMP where id = {}", id);
        logger.debug("execute select query: {}", sql);
        return jdbcClient.sql(sql)
                .query(BootcampModel.class)
                .optional()
                .orElseThrow(NotFoundResourceWhileFetchingException::new);
    }
}
