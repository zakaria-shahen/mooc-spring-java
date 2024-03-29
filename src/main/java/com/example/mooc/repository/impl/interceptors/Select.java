package com.example.mooc.repository.impl.interceptors;

import java.util.List;


public record Select (
        List<String> fields
) {
    public Select(String ...fields) {
         this(List.of(fields));
    }
}