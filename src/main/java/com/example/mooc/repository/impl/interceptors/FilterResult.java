package com.example.mooc.repository.impl.interceptors;

import com.example.mooc.exception.SelectFieldNameNotExists;
import com.example.mooc.repository.impl.interceptors.specification.JdbcClientSqlInterceptorWith;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FilterResult implements JdbcClientSqlInterceptorWith<FilterBy> {

    public String intercept(String sql, FilterBy filterBy) {
        if (filterBy == null || filterBy.asNames().isEmpty()) {
            return sql;
        }

        String availableColumns = extreactAvailableColumns(sql);

        sql = STR."\{sql} \{where(availableColumns, filterBy)}";
        return sql;
    }

    private String where(String availableColumns, FilterBy filterBy) {
        final int DELETE_LAST_OPERATOR = 5;
        var where = STR."where \{filterBy.asNames().stream().map(it -> STR."\{checkSqlInject(availableColumns, it)} = ? and ")
                .collect(Collectors.joining(""))}";

        where = where.substring(0, where.length() - DELETE_LAST_OPERATOR);
        return where;

    }

    private String checkSqlInject(String availableColumns, String columnName) {

        if (availableColumns.contains(columnName) && columnName.toLowerCase().matches("^[a-z_]+$")) {
            return columnName;
        }
        throw new SelectFieldNameNotExists();
    }

    private String extreactAvailableColumns(String sql) {
        var matcher = Pattern.compile("(!selectFields|!filterByAndSelectFields)\\((.+)\\)").matcher(sql);
        if (!matcher.find()) {
            return sql;
        }
        return matcher.group(2);
    }

}
