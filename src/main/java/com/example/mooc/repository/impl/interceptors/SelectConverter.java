package com.example.mooc.repository.impl.interceptors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converter to enable the use of list data types as query parameter values.
 * formal syntax: `queryKayName=value,value`
 */
@Component
public class SelectConverter implements Converter<String, Select> {

    @Override
    public Select convert(String selectParameter) {
        if (selectParameter == null) {
            return new Select(List.of());
        }
        var listOfFiled = List.of(selectParameter.split(","));
        return new Select(listOfFiled);
    }

}
