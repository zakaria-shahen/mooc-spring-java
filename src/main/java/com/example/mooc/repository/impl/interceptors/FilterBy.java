package com.example.mooc.repository.impl.interceptors;

import java.util.List;


public record FilterBy(
        List<String> asNames,
        List<String> asValues
) {

}