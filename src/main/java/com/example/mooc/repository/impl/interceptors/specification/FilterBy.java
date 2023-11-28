package com.example.mooc.repository.impl.interceptors.specification;

import java.util.List;


public record FilterBy(
        Integer size,
        List<String> asParams
) {

}