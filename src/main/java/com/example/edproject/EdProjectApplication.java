package com.example.edproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.edproject.mapper")
public class EdProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdProjectApplication.class, args);
	}

}
