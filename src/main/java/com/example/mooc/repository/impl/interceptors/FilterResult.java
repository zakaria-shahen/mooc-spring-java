package com.example.mooc.repository.impl.interceptors;

import com.example.mooc.repository.impl.interceptors.specification.JdbcClientSqlInterceptor;
import org.springframework.jdbc.support.JdbcUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FilterResult implements JdbcClientSqlInterceptor {
    public String intercept(String sql) {
        // match to get !filter(N) and N in list
        // RegEx: !filter\((.+)\)
        var matcher = Pattern.compile("!filter\\((.+)\\)").matcher(sql);
        if (!matcher.find()) {
            return sql;
        }
        var filterStatement = matcher.group(0);
        var numberOfParameters = Integer.valueOf(matcher.group(1));

        sql = sql.replace(filterStatement, "");
        sql = STR."select * from (\{sql}) \{where(numberOfParameters)}";
        return sql;
    }

    private String where(Integer size) {
        if (size == 0) {
            return "";
        }

        final int DELETE_LAST_OPERATOR = 5;
        var where = STR."""
                  where \{"? = ? and".repeat(size)}
                """;
        where = where.substring(0, where.length() - DELETE_LAST_OPERATOR);
        return where;
    }

    @Deprecated
    public static List<String> parserFilterParameters(Map<String, String> by) {
        var filtersValues = new ArrayList<String>();
        by.forEach((key, value) ->  {
            filtersValues.add(JdbcUtils.convertPropertyNameToUnderscoreName(key));
            filtersValues.add(value);
        });
        return filtersValues;
    }

}
