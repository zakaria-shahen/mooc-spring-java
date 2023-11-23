package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileDeletingException;
import com.example.mooc.exception.NotFoundResourceWhileFetchingException;
import com.example.mooc.model.ReviewModel;
import com.example.mooc.repository.ReviewRepository;
import com.example.mooc.utils.SqlUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final JdbcClient jdbcClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ReviewModel create(ReviewModel reviewModel) {
        var sql = "insert into REVIEW(#id, #title, #text, #rating, #course_id, #user_id) values(@@)";
        sql = SqlUtils.addNamedParameters(sql);
        logger.info("trying execute insert query against REVIEW for review title -> {}", reviewModel.getTitle());
        logger.debug("execute insert query: {}", sql);
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .paramSource(reviewModel)
                .update(keyHolder, "id");

        reviewModel.setId(SqlUtils.getKeyHolderValue(keyHolder, logger));
        return reviewModel;
    }

    @Override
    public ReviewModel update(ReviewModel reviewModel) {
        var sql = """
                update REVIEW set #title = @, #text = @, #rating = @
                where #id = @ and #course_id = @ and #user_id = @
                """.strip();
        sql = SqlUtils.addNamedParameters(sql);
        logger.info("trying updating query against REVIEW for review id -> {}, title -> {}, user id -> {}", reviewModel.getId(), reviewModel.getTitle(), reviewModel.getUserId());
        logger.debug("execute update query: {}", sql);
        var affectRows = jdbcClient.sql(sql)
                .paramSource(reviewModel)
                .update();

        SqlUtils.validateAffectRows(affectRows, reviewModel.getId(), "REVIEW", logger);
        return reviewModel;
    }

    @Override
    public Boolean delete(ReviewModel reviewModel) {
        return deleteById(reviewModel.getId(), reviewModel.getCourseId());
    }

    @Override
    public Boolean deleteById(Long id, Long userId) {
        var sql = "delete from REVIEW where id = ? and user_id = ?";
        var affectRows = jdbcClient.sql(sql)
                .param(id)
                .params(userId)
                .update();
        if (affectRows != 1) {
            logger.debug("Fail, while deleting REVIEW ID={}, user id={}, affectRows={}", id, userId, affectRows);
            throw new NotFoundResourceWhileDeletingException();
        }
        logger.info("Successfully deleting REVIEW ID={}, user id={}", id, userId);
        return true;
    }

    @Override
    public ReviewModel findById(Long id) {
        var sql = "select id, title, text, rating, course_id, user_id from REVIEW where id = ?";
        logger.info("trying to fetch one COURSE where id = {}", id);
        logger.debug("execute select query: {}", sql);
        return jdbcClient.sql(sql)
                .param(id)
                .query(ReviewModel.class)
                .optional()
                .orElseThrow(NotFoundResourceWhileFetchingException::new);
    }

    @Override
    public List<ReviewModel> findAllByCourseId(Long courseId) {
        var sql = """
               select id, title, text, rating, course_id, user_id
               from REVIEW
               where course_id = ?
               """.strip();
        return jdbcClient.sql(sql)
                .param(courseId)
                .query(ReviewModel.class)
                .list();
    }
}
