package com.example.mooc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CareerModel {
     BACK_END(1, "back-end"), DEVOPS(2, "devops");

    private final int id;
    private final String name;
}
