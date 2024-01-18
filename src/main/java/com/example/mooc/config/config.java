package com.example.mooc.config;

import com.example.mooc.repository.impl.interceptors.AddNamedParameters;
import com.example.mooc.repository.impl.interceptors.FilterResult;
import com.example.mooc.repository.impl.interceptors.Paging;
import com.example.mooc.repository.impl.interceptors.SelectFields;
import com.example.mooc.repository.impl.interceptors.specification.CustomJdbcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class config {

    @Bean
    CustomJdbcClient customJdbcClient(JdbcTemplate jdbcTemplate) {
         return new CustomJdbcClient(jdbcTemplate, new Paging(), new FilterResult(), new SelectFields())
                 .addInterceptor(new AddNamedParameters());
    }


}
