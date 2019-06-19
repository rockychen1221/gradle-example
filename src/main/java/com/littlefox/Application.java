package com.littlefox;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
@MapperScan("com.littlefox.**.dao")
public class Application {

	public static void main(String[] args) {
		log.info("Spring Boot Strat Loading...");
		SpringApplication.run(Application.class, args);
		log.info("Spring Boot Strat Loading End...");
	}
}
