package com.example.mooc.utils;

import org.springframework.jdbc.support.JdbcUtils;

import java.util.regex.Pattern;

public class SqlUtils {

    private SqlUtils() {
    }

    /***
     * @param sql: String have #SQL_column_name and @@ or @ as Named Parameter Position
     * @return SQL have NamedParameters
     */
    public static String addNamedParameters(String sql) {
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
            namedParameters = namedParameters.substring(0, namedParameters.length() - 2);

            sql = sql.replace("@@", namedParameters);
        } else {
            while (matcher.find()) {
                sql = sql.replaceFirst("@", STR.":\{JdbcUtils.convertUnderscoreNameToPropertyName(matcher.group())}");
            }
        }
        return sql.replace("#", "");
    }
}
