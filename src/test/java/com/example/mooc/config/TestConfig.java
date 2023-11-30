package com.example.mooc.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.OracleContainer;

@Configuration
public class TestConfig {

    @Bean
    @ServiceConnection
    public OracleContainer oracleContainer() {
        return new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
                .withReuse(true)
                .withStartupTimeoutSeconds(150);
    }



}
