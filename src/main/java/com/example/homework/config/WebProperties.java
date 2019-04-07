package com.example.homework.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class WebProperties {
    @Value("${page.size}")
    public int pageSize;
    @Value("${page.init}")
    public int initPage;
    @Value("${api.gameOfThrones.url}")
    public String gameOfThronesUrl;
}
