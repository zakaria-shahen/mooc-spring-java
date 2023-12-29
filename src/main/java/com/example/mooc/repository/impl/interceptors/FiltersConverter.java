package com.example.mooc.repository.impl.interceptors;

import org.springframework.core.convert.converter.Converter;
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
            return new FilterBy(0, Collections.emptyList());
        }
        List<String> asParams = convertToList(source);
        int size = asParams.isEmpty() ? 0 : asParams.size() / 2;
        return new FilterBy(size, asParams);
    }

    private List<String> convertToList(String input) {
        var list = new ArrayList<String>();
        for (String it : input.split(",")) {
            var keyValues = it.split(":");
            list.add(keyValues[0]);
            list.add(keyValues[1]);
        }
        return list;
    }

}
