package com.example.mooc;

import com.example.mooc.config.TestConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MoocApplicationTests {

	public static void main(String[] args) {
		SpringApplication.from(MoocApplication::main)
				.with(TestConfig.class)
				.run(args);
	}

}
