package com.example.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport
@EnableConfigurationProperties
public class SpringBootGameOfThronesApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootGameOfThronesApplication.class, args);
    }

}
