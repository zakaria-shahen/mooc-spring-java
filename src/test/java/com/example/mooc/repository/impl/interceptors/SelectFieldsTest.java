package com.example.mooc.repository.impl.interceptors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class SelectFieldsTest {

    @Test
    void testing() {
        var input = "select !selectFields(id, user_id as user, name as    n, user_id, rating) from bootcamp";
        var output = new SelectFields().intercept(input, new Select(List.of("id", "user", "n"))).strip();
        Assertions.assertThat(output).isEqualTo(("select id, user_id as user, name as    n from bootcamp").strip());

        input = "select !filterByAndSelectFields(id, user_id as user, name as    n, user_id, rating) from bootcamp";
        output = new SelectFields().intercept(input, new Select(List.of("id", "user", "n"))).strip();
        Assertions.assertThat(output).isEqualTo(("select id, user_id as user, name as    n from bootcamp").strip());

    }
}