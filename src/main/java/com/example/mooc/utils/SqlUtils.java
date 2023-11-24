package com.example.mooc.utils;

import com.example.mooc.exception.NotFoundResourceWhileUpdatingException;
import com.example.mooc.exception.SomethingWantWrongWhileFetchingIdException;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;

import java.util.regex.Pattern;

public class SqlUtils {

    private SqlUtils() {
    }

    public static Long getKeyHolderValue(KeyHolder keyHolder, Logger logger) {
        var id = keyHolder.getKey();
        if (id == null) {
            logger.debug("Fail, unable to get resource ID!, keyHolder value is NULL");
            throw new SomethingWantWrongWhileFetchingIdException();
        }
        return id.longValue();

    }

    public static void validateAffectRows(int affectRows, Long id, String typeName, Logger logger) {
        if (affectRows != 1) {
            logger.error("Fail, while updating {} ID={}, affectRow={}", typeName, id, affectRows);
            throw new NotFoundResourceWhileUpdatingException();
        }
        logger.info("1 affect row, {} ID={}", typeName, id);
    }
}
