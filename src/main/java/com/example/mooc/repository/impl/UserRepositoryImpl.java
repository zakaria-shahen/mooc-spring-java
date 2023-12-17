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

    @Override
    public UserModel findByEmail(String email) {
        return jdbcClient
                .sql("select id, name, email, password, status, login_attempts, role from USER_ where email = ?")
                .param(email)
                .query(UserModel.class)
                .optional()
                .orElseThrow(NotFoundResourceWhileFetchingException::new);
    }
}
