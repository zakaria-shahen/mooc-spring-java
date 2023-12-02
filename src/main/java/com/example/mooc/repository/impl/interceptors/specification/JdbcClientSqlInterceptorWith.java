package com.example.mooc.repository.impl.interceptors.specification;

@FunctionalInterface
public interface JdbcClientSqlInterceptorWith<T> {

    String intercept(String sql, T input);

}