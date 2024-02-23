package com.syh.gptblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GptblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(GptblogApplication.class, args);
	}

}
