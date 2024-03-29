package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileDeletingException;
import com.example.mooc.exception.NotFoundResourceWhileFetchingException;
import com.example.mooc.model.CourseModel;
import com.example.mooc.repository.CourseRepository;
import com.example.mooc.repository.impl.interceptors.specification.CustomJdbcClient;
import com.example.mooc.utils.SqlUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final Logger logger = LoggerFactory.getLogger(CourseRepositoryImpl.class);
    private final CustomJdbcClient jdbcClient;

    @Override
    public CourseModel create(CourseModel courseModel, Boolean isAdmin) {
        var sql = STR."""
                    insert into COURSE(#title, #description, #weeks, #tuition, #minimum_skill, #cost, #bootcamp_id, user_id)
                    values(@@, (select :userId from dual where check_user_onw_bootcamp(:bootcampId, :userId, \{isAdmin? 1 : 0}) = 1))
                """;
        logger.info("trying execute insert query against COURSE for course title -> {}", courseModel.getTitle());
        logger.debug("execute insert query: {}", sql);
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .paramSource(courseModel)
                .update(keyHolder, "id");

        courseModel.setId(SqlUtils.getKeyHolderValue(keyHolder, logger));
        return courseModel;
    }

    @Override
    public CourseModel update(CourseModel courseModel, Boolean isAdmin) {
        var sql = STR."""
                    update COURSE SET #title = @, #description = @, #weeks = @, #tuition = @, #minimum_skill = @, #cost= @
                    where #id = @ and (#user_id = @ or 1 = \{isAdmin? 1 : 0})
                """.strip();
        logger.info("trying updating query against COURSE for Course id -> {}, user id -> {}", courseModel.getId(), courseModel.getUserId());
        logger.debug("execute update query: {}", sql);
        var affectRows = jdbcClient.sql(sql)
                .paramSource(courseModel)
                .update();
        SqlUtils.validateAffectRows(affectRows, courseModel.getId(), "COURSE", logger);
        return courseModel;
    }

    @Override
    public Boolean delete(CourseModel courseModel) {
        return delete(courseModel.getId(), courseModel.getUserId(), false);
    }

    @Override
    public Boolean delete(Long courseId, Long userId, Boolean isAdmin) {
        var sql = """
            begin 
                delete from REVIEW where COURSE_ID = ?;
                delete from COURSE where id = ? and (user_id = ? or 1 = ?);
                if SQL%ROWCOUNT = 0 then
                    raise_application_error(-20000, 'not found course');
                end if;
           end;
        """;
        var affectRows = jdbcClient.sql(sql)
                .param(courseId)
                .param(courseId)
                .param(userId)
                .param(isAdmin? 1 : 0)
                .update();
        if (affectRows != 1) {
            logger.debug("Fail, while deleting COURSE ID={}, user id={}, affectRows={}", courseId, userId, affectRows);
            throw new NotFoundResourceWhileDeletingException();
        }
        logger.info("Successfully deleting COURSE, ID={}, user id={}", courseId, userId);
        return true;
    }

    @Override
    public List<CourseModel> findAllByBootcampId(Long bootcampId, Pageable pageable) {
        var sql = "select id, title, description, weeks, tuition, minimum_skill, cost, bootcamp_id, user_id from COURSE where bootcamp_id = ?";
        logger.info("trying to fetch all COURSE");
        logger.debug("execute select query: {}", sql);
        return jdbcClient.pageable(pageable)
                .sql(sql)
                .param(bootcampId)
                .query(CourseModel.class)
                .list();
    }

    @Override
    public CourseModel findById(Long id) {
        var sql = "select id, title, description, weeks, tuition, minimum_skill, cost, bootcamp_id, user_id from COURSE where id = ?";
        logger.info("trying to fetch one COURSE where id = {}", id);
        logger.debug("execute select query: {}", sql);
        return jdbcClient.sql(sql)
                .param(id)
                .query(CourseModel.class)
                .optional()
                .orElseThrow(NotFoundResourceWhileFetchingException::new);
    }
}
