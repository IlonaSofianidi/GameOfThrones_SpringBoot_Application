package com.example.homework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootGameOfThronesApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootGameOfThronesApplication.class, args);
	}

}
