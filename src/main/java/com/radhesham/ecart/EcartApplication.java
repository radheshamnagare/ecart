package com.radhesham.ecart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ComponentScan
@PropertySources(value = {@PropertySource("classpath:application.properties"),
        @PropertySource("classpath:commonConfig.properties"),
        @PropertySource("classpath:db.properties"),
        @PropertySource("classpath:mailConfig.properties")})
public class EcartApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcartApplication.class, args);
    }

}
