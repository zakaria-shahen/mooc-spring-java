package com.example.mooc.config;

import com.example.mooc.repository.impl.Interceptors.AddNamedParameters;
import com.example.mooc.repository.impl.Interceptors.Paging;
import com.example.mooc.repository.impl.Interceptors.specification.CustomJdbcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class config {

    @Bean
    CustomJdbcClient customJdbcClient(JdbcTemplate jdbcTemplate) {
         return new CustomJdbcClient(jdbcTemplate, Paging::interceptor)
                 .addInterceptor(new AddNamedParameters());
    }


}
