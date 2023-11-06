package com.example.mooc.repository.impl;

import com.example.mooc.model.BootcampModel;
import com.example.mooc.repository.BootcampRepository;
import com.example.mooc.utils.SQLQueryBuilderUtils;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BootcampRepositoryImpl implements BootcampRepository {

    private final JdbcClient jdbcClient;
    private final SQLQueryBuilderUtils bootcampModelSqlUtils = BootcampModel.sqlModuleUtils;

    public BootcampRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public BootcampModel create(@NonNull BootcampModel bootcampModel) {
        String sql = STR."""
            insert into BOOTCAMP(\{bootcampModelSqlUtils.parameterNames})
            values(\{bootcampModelSqlUtils.placeholderParameters})
        """.trim();
        var keyHolder = new GeneratedKeyHolder();
        System.out.println(sql);
        var id = jdbcClient.sql(sql)
                .param(bootcampModel)
                .update(keyHolder);
        return bootcampModel;
    }


    @Override
    public BootcampModel update(@NonNull BootcampModel bootcampModel) {
        var sql = STR."update BOOTCAMP SET \{bootcampModelSqlUtils.fieldsNamesForUpdate} where id = ?";
        var isUpdated = jdbcClient.sql(sql)
                .param(bootcampModel)
                .param(bootcampModel.id())
                .update() == 1;
        if (isUpdated) {
            throw new RuntimeException("Not found id when trying to updating");
        }
        return bootcampModel;
    }

    @Override
    public Boolean delete(@NonNull BootcampModel bootcampModel) {
        return delete(bootcampModel.id());
    }

    @Override
    public Boolean delete(@NonNull Long id) {
        var sql = STR."delete BOOTCAMP where id = ?";
        return jdbcClient.sql(sql)
                .param(id)
                .update() == 1;
    }

    @Override
    public List<BootcampModel> findAll() {
         var sql = STR."select id, \{bootcampModelSqlUtils.parameterNames} from BOOTCAMP";
         return jdbcClient.sql(sql)
                 .query(BootcampModel.class)
                 .list();
    }

    @Override
    public BootcampModel findById(@NonNull Long id) {
        var sql = STR."select id, \{bootcampModelSqlUtils.parameterNames} from BOOTCAMP";
         return jdbcClient.sql(sql)
                 .query(BootcampModel.class)
                 .single();
    }
}
