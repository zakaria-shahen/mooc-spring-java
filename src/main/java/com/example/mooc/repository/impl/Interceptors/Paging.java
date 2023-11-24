package com.example.mooc.repository.impl.Interceptors;

import org.springframework.data.domain.Pageable;

public class Paging {
    public static String interceptor(String sql, Pageable pageable) {
        long offset = pageable.getOffset(); // to make sure it's long or int always, to prevent SQL injection
        int pageSize = pageable.getPageSize(); // to make sure it's long or int always, to prevent SQL injection
        return STR."\{sql} offset \{offset} rows fetch next \{pageSize} rows only";
    }
}
