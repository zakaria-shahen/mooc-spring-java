package com.example.mooc.repository.impl;

import com.example.mooc.exception.NotFoundResourceWhileFetchingException;
import com.example.mooc.model.OtpModel;
import com.example.mooc.repository.OtpRepository;
import com.example.mooc.repository.impl.interceptors.specification.CustomJdbcClient;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@AllArgsConstructor
@Repository
public class OtpRepositoryImpl implements OtpRepository {

    private final CustomJdbcClient jdbcClient;

    @Override
    public OtpModel findById(Long id) {
        return jdbcClient.sql("""
                        select id, receiver, otp, attempts, used, type, create_at, expiration_after_seconds from OTP
                        where id = ?
                        """)
                .param(id)
                .query(OtpModel.class)
                .optional()
                .orElseThrow(NotFoundResourceWhileFetchingException::new);

    }

    @Override
    public OtpModel create(OtpModel otpModel) {
        var keyHolder = new GeneratedKeyHolder();
         jdbcClient.sql("insert into OTP(#receiver, #otp, #type, #create_at) values (@@)")
                 .paramSource(otpModel)
                 .update(keyHolder, "id");

         otpModel.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return otpModel;
    }

    @Override
    public boolean incrementAttemptsById(Long id) {
         return jdbcClient.sql("update OTP set attempts = attempts + 1 where id = ?")
                 .param(id)
                 .update() == 1;

    }

    @Override
    public boolean updateOtpToUsedById(Long id) {
        return jdbcClient.sql("update OTP set used = 1 where id = ?")
                .param(id)
                .update() == 1;
    }
}
