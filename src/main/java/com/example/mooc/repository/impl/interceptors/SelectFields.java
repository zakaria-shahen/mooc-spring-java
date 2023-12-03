package com.example.mooc.repository.impl.interceptors;

import com.example.mooc.repository.impl.interceptors.specification.JdbcClientSqlInterceptorWith;
import org.springframework.jdbc.support.JdbcUtils;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SelectFields implements JdbcClientSqlInterceptorWith<Select> {

    @Override
    public String intercept(String sql, Select stringList) {
        // input: select !selectFields(id, user_id, name as n) from bootcamp
        // output: select id, user_id, name from bootcamp;

        var matcher = Pattern.compile("!selectFields\\((.+)\\)").matcher(sql);
        if (!matcher.find()) {
            return sql;
        }
        var selectFieldsStatement = matcher.group(0);
        var availableColumns = matcher.group(1);

        if (stringList == null || stringList.fields().isEmpty()) {
             return sql.replace(selectFieldsStatement, availableColumns);
        }

        var availableColumnsList = Arrays.stream(availableColumns.split(","))
                .map(String::strip)
                .toList();
        var selectFields =  availableColumnsList.stream()
                .filter(it -> stringList.fields().contains(extractAliasIfExists(JdbcUtils.convertPropertyNameToUnderscoreName(it))))
                .collect(Collectors.joining(", "));

        sql = sql.replace(selectFieldsStatement, selectFields);

        return sql;
    }

    private static String extractAliasIfExists(String it) {
        var as = " as ";
        var index = it.toLowerCase().indexOf(as);
        if (index == -1) {
            return it;
        }
        return it.substring(index + as.length()).strip();
    }
}
