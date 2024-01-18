package com.example.mooc.repository.impl.interceptors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Converter to enable the use of map data types as query parameter values.
 * formal syntax: `queryKayName=kay:value,key:value`
 */
@Component
public class FiltersConverter implements Converter<String, FilterBy> {

    @Override
    public FilterBy convert(String source) {
        if (source.isEmpty()) {
            return new FilterBy(Collections.emptyList(), Collections.emptyList());
        }
        List<String> asValues = new ArrayList<>();
        List<String> asNames = new ArrayList<>();
        convertToList(source, asNames, asValues);
        return new FilterBy(asNames, asValues);
    }

    private void convertToList(String input, List<String> asNames, List<String> asValues) {
        for (String it : input.split(",")) {
            var keyValues = it.split(":");
            asNames.add(JdbcUtils.convertPropertyNameToUnderscoreName(keyValues[0]));
            asValues.add(keyValues[1]);
        }
    }

}
