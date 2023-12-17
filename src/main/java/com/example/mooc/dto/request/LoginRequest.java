package com.example.mooc.dto.request;

public record LoginRequest(
        String principal,
        String credentials
) { }
