package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileFetchingException;
import com.example.mooc.model.UserModel;
import com.example.mooc.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcClient jdbcClient;
    private final static String SQL_SELECT_BY_EMAIL = "select id, name, email, status, login_attempts from USER_ where email = ?";

    @Override
    public UserModel findByEmail(String email) {
        return findOneOrThrow(SQL_SELECT_BY_EMAIL, email);
    }

    @Override
    public UserModel findByEmailAndPassword(String email, String password) {
        return findOneOrThrow(STR."\{SQL_SELECT_BY_EMAIL} and password = ?", email, password);
    }

    private UserModel findOneOrThrow(String sql, Object ...values) {
        return jdbcClient.sql(sql)
                .params(values)
                .query(UserModel.class)
                .optional()
                .orElseThrow(NotFoundResourceWhileFetchingException::new);
    }
}
