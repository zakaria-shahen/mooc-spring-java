package com.example.mooc.repository.impl.Interceptors.specification;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class CustomJdbcClient implements JdbcClient {
    private final JdbcClient jdbcClient;
    private final Map<String, String> parserSqlCache = new ConcurrentHashMap<>();
    private final List<JdbcClientSqlInterceptor> interceptorList = new ArrayList<>();
    private final BiFunction<String, Pageable, String> pagingParser;

    public CustomJdbcClient(JdbcTemplate jdbcTemplate, BiFunction<String, Pageable, String> pagingParser) {
        this.jdbcClient = JdbcClient.create(jdbcTemplate);
        this.pagingParser = pagingParser;
    }

    public CustomJdbcClient addInterceptor(@Nonnull JdbcClientSqlInterceptor interceptor) {
        interceptorList.add(interceptor);
        return this;
    }

    @Override
    public StatementSpec sql(String sql) {
        String finalSql = parserSqlCache.computeIfAbsent(sql, this::applyInterceptor);
        return jdbcClient.sql(finalSql);
    }

    public StatementSpec sql(@NotNull String sql, Pageable pageable) {
        String finalSql = parserSqlCache.computeIfAbsent(sql, this::applyInterceptor);
        return jdbcClient.sql(pagingParser.apply(finalSql, pageable));
    }

    private String applyInterceptor(String sql) {
        var parserSql = sql;
        for (JdbcClientSqlInterceptor it : interceptorList) {
            parserSql = it.intercept(parserSql);
        }
        return parserSql;
    }
}



