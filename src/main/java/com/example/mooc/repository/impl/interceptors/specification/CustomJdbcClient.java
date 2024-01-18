package com.example.mooc.repository.impl.interceptors.specification;

import com.example.mooc.repository.impl.interceptors.*;
import jakarta.annotation.Nonnull;
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
    private final FilterResult filterResultInterceptor;

    private FilterBy filterBy = null;
    private Pageable pageable = null;
    private Select selectFields;


    public CustomJdbcClient(JdbcTemplate jdbcTemplate, Paging pagingParser, FilterResult filterResult, SelectFields selectFields) {
        this.jdbcClient = JdbcClient.create(jdbcTemplate);
        this.pagingParserInterceptor = pagingParser;
        this.filterResultInterceptor = filterResult;
        this.selectFieldsInterceptor = selectFields;
    }

    public CustomJdbcClient addInterceptor(@Nonnull JdbcClientSqlInterceptor interceptor) {
        interceptorList.add(interceptor);
        return this;
    }

    @Override
    public StatementSpec sql(String sql) {
        String finalSql = parserSqlCache.computeIfAbsent(sql,  this::applyInterceptor);

        finalSql = filterResultInterceptor.intercept(finalSql, filterBy);
        filterBy = null;

        finalSql = selectFieldsInterceptor.intercept(finalSql, selectFields);
        selectFields = null;

        finalSql = pagingParserInterceptor.intercept(finalSql, pageable);
        pageable = null;

        return jdbcClient.sql(finalSql);
    }

    public CustomJdbcClient filterBy(FilterBy filterBy) {
        this.filterBy = filterBy;
        return this;
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



