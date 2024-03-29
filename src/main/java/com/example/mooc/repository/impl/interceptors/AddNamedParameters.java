package com.example.mooc.repository.impl.interceptors;

import com.example.mooc.repository.impl.interceptors.specification.JdbcClientSqlInterceptor;
import org.springframework.jdbc.support.JdbcUtils;

import java.util.regex.Pattern;

public class AddNamedParameters implements JdbcClientSqlInterceptor {

    /**
     * Automatically add named parameters in SQL insert or insert statements.<br/>
     * You should add prefix `#` before echo sql column name, and add `@` or `@@` to select position where named Parameters.<br/>
     * Example.<br/>
     * - `insert into my_table(#name, #description) values(@@)`<br/>
     * - `update my_table set #name = @, #description = @ where id = ?`
     * @param sql: String SQL insert or update statements
     * @return SQL have NamedParameters
     */
    @Override
    public String intercept(String sql) {
        // Link -> #
        // replace all -> @@
        // replace per # -> @
        var matcher = Pattern.compile("(?<=#).\\w+").matcher(sql);
        if (sql.contains("@@")) {
            var namedParameters = "";
            while (matcher.find()) {
                namedParameters += STR.":\{JdbcUtils.convertUnderscoreNameToPropertyName(matcher.group())}, ";
            }
            // remove last ", "
            if (!namedParameters.isEmpty()) {
                namedParameters = namedParameters.substring(0, namedParameters.length() - 2);
            }
            sql = sql.replace("@@", namedParameters);
        } else {
            while (matcher.find()) {
                sql = sql.replaceFirst("@", STR.":\{JdbcUtils.convertUnderscoreNameToPropertyName(matcher.group())}");
            }
        }
        return sql.replace("#", "");
    }

}
