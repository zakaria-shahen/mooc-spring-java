package com.example.mooc.repository.impl.Interceptors.specification;

@FunctionalInterface
public interface JdbcClientSqlInterceptor {

    String intercept(String sql);

}