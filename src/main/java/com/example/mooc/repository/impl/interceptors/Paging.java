package com.example.mooc.repository.impl.interceptors;

import com.example.mooc.repository.impl.interceptors.specification.JdbcClientSqlInterceptorWith;
import org.springframework.data.domain.Pageable;

public class Paging implements JdbcClientSqlInterceptorWith<Pageable> {

    @Override
    public String intercept(String sql, Pageable pageable) {
        long offset = pageable.getOffset(); // to make sure it's long or int always, to prevent SQL injection
        int pageSize = pageable.getPageSize(); // to make sure it's long or int always, to prevent SQL injection
        return STR."\{sql} offset \{offset} rows fetch next \{pageSize} rows only";
    }
}
