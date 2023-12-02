package com.example.mooc.repository.impl.interceptors.specification;

import com.example.mooc.repository.impl.interceptors.Paging;
import com.example.mooc.repository.impl.interceptors.Select;
import com.example.mooc.repository.impl.interceptors.SelectFields;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomJdbcClient implements JdbcClient {
    private final JdbcClient jdbcClient;
    private final Map<String, String> parserSqlCache = new ConcurrentHashMap<>();
    private final List<JdbcClientSqlInterceptor> interceptorList = new ArrayList<>();
    private final Paging pagingParserInterceptor;
    private final SelectFields selectFieldsInterceptor;

    private Pageable pageable;
    private Select selectFields;

    public CustomJdbcClient(JdbcTemplate jdbcTemplate, Paging pagingParser, SelectFields selectFields) {
        this.jdbcClient = JdbcClient.create(jdbcTemplate);
        this.pagingParserInterceptor = pagingParser;
        this.selectFieldsInterceptor = selectFields;
    }

    public CustomJdbcClient addInterceptor(@Nonnull JdbcClientSqlInterceptor interceptor) {
        interceptorList.add(interceptor);
        return this;
    }

    @Override
    public StatementSpec sql(String sql) {
        String finalSql = parserSqlCache.computeIfAbsent(sql, this::applyInterceptor);
        if (pageable != null) {
            finalSql = pagingParserInterceptor.intercept(finalSql, pageable);
        }

        if (selectFields != null) {
            finalSql = selectFieldsInterceptor.intercept(sql, selectFields);
        }

        return jdbcClient.sql(finalSql);
    }

    @Deprecated
    public StatementSpec sql(@NotNull String sql, Pageable pageable) {
        String finalSql = parserSqlCache.computeIfAbsent(sql, this::applyInterceptor);
        return jdbcClient.sql(pagingParserInterceptor.intercept(finalSql, pageable));
    }

    public CustomJdbcClient pageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    public CustomJdbcClient selectFields(Select selectFields) {
        this.selectFields = selectFields;
        return this;
    }

    private String applyInterceptor(String sql) {
        var parserSql = sql;
        for (JdbcClientSqlInterceptor it : interceptorList) {
            parserSql = it.intercept(parserSql);
        }
        return parserSql;
    }
}



