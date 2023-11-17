package com.example.mooc.utils;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class SQLQueryBuilderUtils {
    public final String fieldsNamesForUpdate;
    public final String parameterNames;
    public final String placeholderParameters;

    public SQLQueryBuilderUtils(Class<?> module) {
        var fieldsNameList = getFieldsNamesWithoutId(module);
        parameterNames = String.join(", ", fieldsNameList);
        fieldsNamesForUpdate = getFieldsNamesForUpdateSql(fieldsNameList);
        placeholderParameters = SQLQueryBuilderUtils.placeholderParametersMaker(fieldsNameList);
    }

    private static String getFieldsNamesForUpdateSql(List<String> fieldsNames) {
        return fieldsNames.stream()
                .map(it -> STR."\{it} = ?")
                .collect(Collectors.joining(", "));
    }

    private static List<String> getFieldsNamesWithoutId(Class<?> module) {
        return Arrays.stream(module.getDeclaredFields()).filter(it -> !it.accessFlags().contains(AccessFlag.STATIC))
                .map(Field::getName)
                .filter(it -> !it.equals("id"))
                .toList();
    }

    private static String placeholderParametersMaker(List<String> fieldsNames) {
        String m = "?, ".repeat(fieldsNames.size());
        return m.substring(0, m.length() - 2);
    }
}
