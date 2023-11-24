package com.example.mooc.repository.impl.interceptors.specification;

@FunctionalInterface
public interface JdbcClientSqlInterceptor {

    String intercept(String sql);

}