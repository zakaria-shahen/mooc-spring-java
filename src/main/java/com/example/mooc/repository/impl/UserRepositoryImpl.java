package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileFetchingException;
import com.example.mooc.model.UserModel;
import com.example.mooc.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
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

    @Override
    public boolean incrementLoginAttempts(String email) {
        return jdbcClient.sql("update USER_ set login_attempts = login_attempts+1 where email = ?")
                .param(email)
                .update() == 1;
    }

    @Override
    public boolean resetLoginAttempts(String email) {
        return jdbcClient.sql("update USER_ set login_attempts = 0 where email = ?")
                .param(email)
                .update() == 1;
    }

    @Override
    public UserModel addUser(UserModel userModel) {
        var key = new GeneratedKeyHolder();
        jdbcClient.sql("insert into USER_(#name, #email, #password, #status, #login_attempts, #role) values(@@)")
                .paramSource(userModel)
                .update(key, "id");
        userModel.setId(key.getKey().longValue());
        return userModel;

    }
}
