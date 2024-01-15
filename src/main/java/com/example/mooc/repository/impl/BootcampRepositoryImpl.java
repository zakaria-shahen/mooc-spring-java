package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileDeletingException;
import com.example.mooc.exception.NotFoundResourceWhileFetchingException;
import com.example.mooc.exception.NotFoundResourceWhileUpdatingException;
import com.example.mooc.exception.SomethingWantWrongWhileFetchingIdException;
import com.example.mooc.model.BootcampModel;
import com.example.mooc.repository.BootcampRepository;
import com.example.mooc.repository.impl.interceptors.FilterBy;
import com.example.mooc.repository.impl.interceptors.Select;
import com.example.mooc.repository.impl.interceptors.specification.CustomJdbcClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class BootcampRepositoryImpl implements BootcampRepository {

    private final Logger logger = LoggerFactory.getLogger(BootcampRepositoryImpl.class);
    private final CustomJdbcClient jdbcClient;

    @Override
    public BootcampModel create(@NonNull BootcampModel bootcampModel) {
        String sql = """
            insert into BOOTCAMP(#name, #description, #website, #phone, #email, #address, #housing, #job_assistance, #job_guarantee, #average_cost, #average_rating, #user_id)
            values(@@)
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
                #name = @,
                #description = @,
                #website = @,
                #phone = @,
                #email = @,
                #address = @,
                #housing = @,
                #job_assistance = @,
                #job_guarantee = @,
                #average_cost = @,
                #average_rating = @
            where id = :id and user_id = :userId
        """.strip();
        logger.info("trying updating query against BOOTCAMP for bootcamp id -> {}", bootcampModel.getId());
        logger.debug("execute update query: {}", sql);
        var affectRows = jdbcClient.sql(sql)
                .paramSource(bootcampModel)
                .update();
        if (affectRows != 1) {
            logger.error("Fail, while updating BOOTCAMP ID={}, affectRow={}", bootcampModel.getId(), affectRows);
            throw new NotFoundResourceWhileUpdatingException();
        }
        logger.info("1 affect row, BOOTCAMP ID={}", bootcampModel.getId());
        return bootcampModel;
    }

    @Override
    public Boolean delete(@NonNull BootcampModel bootcampModel, boolean isAdmin) {
        return delete(bootcampModel.getId(), bootcampModel.getUserId(), isAdmin);
    }

    @Override
    public Boolean delete(@NonNull Long id, @NonNull Long userId, boolean isAdmin) {
        //TODO: when upgrade to oracle DB 23c then use `?` only without if condition for `isAdmin`.
        // language=SQL
        var sql = """
                declare
                    have_access number;
                begin
                    have_access := check_user_onw_bootcamp(:id, :userId, :isAdmin);
                    delete from BOOTCAMP_CAREER where BOOTCAMP_ID = :id;
                    delete from BOOTCAMP_PHOTO where BOOTCAMP_ID = :id;
                    delete from REVIEW where COURSE_ID in (select id from course where BOOTCAMP_ID = :id);
                    delete from COURSE where BOOTCAMP_ID = :id;
                    delete from BOOTCAMP where id = :id;
                end;
                """.strip();

        var affectRows = jdbcClient.sql(sql)
                .param("id", id)
                .param("userId", userId)
                .param("isAdmin", isAdmin? 1 : 0)
                .update();
        if (affectRows != 1) {
            logger.debug("Fail, while deleting BOOTCAMP ID={}, affectRows={}", id, affectRows);
            throw new NotFoundResourceWhileDeletingException();
        }
        logger.info("Successfully deleting BOOTCAMP, ID={}", id);
        return true;
    }

    @Override
    public List<BootcampModel> findAll(Pageable pageable, FilterBy filterBy) {
        var sql = STR."""
        select
            id, name, description, website, phone, email, address, housing, job_assistance, job_guarantee, average_cost, average_rating, user_id
        from BOOTCAMP !filter(\{filterBy.size()})
        """.strip();

        logger.info("trying to fetch all BOOTCAMP");
        logger.debug("execute select query: {}", sql);
        return jdbcClient.pageable(pageable)
                .sql(sql)
                .params(filterBy.asParams())
                .query(BootcampModel.class)
                .list();
    }

    @Override
    public List<Map<String, Object>> findAll(Pageable pageable, FilterBy filterBy, Select select) {
        var sql = STR."""
        select
            !selectFields(id, name, description, website, phone, email, address, housing, job_assistance, job_guarantee, average_cost, average_rating, user_id)
        from BOOTCAMP !filter(\{filterBy.size()})
        """.strip();

        logger.info("trying to fetch all BOOTCAMP");
        logger.debug("execute select query: {}", sql);
        return jdbcClient.pageable(pageable)
                .selectFields(select)
                .sql(sql)
                .params(filterBy.asParams())
                .query(new ColumnMapRowMapper())
                .list();
    }

    @Override
    public BootcampModel findById(@NonNull Long id) {
        var sql = """
        select
            !selectFields(id, name, description, website, phone, email, address, housing, job_assistance, job_guarantee, average_cost, average_rating, user_id)
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


    @Override
    public Boolean addPhoto(Long bootcampId, String filePath) {
        var sql = "insert into BOOTCAMP_PHOTO(#bootcamp_id, #photo_path) values (@@)";
        return jdbcClient.sql(sql)
                .param(bootcampId)
                .param(filePath)
                .update() == 1;
    }

    @Override
    public Boolean deletePhoto(Long bootcampId, String filePath) {
        var sql = "delete from BOOTCAMP_PHOTO where bootcamp_id = ? and photo_path = ?";
        var affectRows = jdbcClient.sql(sql)
                .param(bootcampId)
                .param(filePath)
                .update();
        if (affectRows != 1) {
            logger.debug("Fail, while deleting BOOTCAMP_PHOTO bootcamp_id={}, photo_path={} affectRows={}", bootcampId, filePath, affectRows);
            throw new NotFoundResourceWhileDeletingException();
        }
        logger.info("Successfully deleting BOOTCAMP_PHOTO bootcamp_id={}, photo_path={} affectRows={}", bootcampId, filePath, affectRows);
        return true;
    }
}
