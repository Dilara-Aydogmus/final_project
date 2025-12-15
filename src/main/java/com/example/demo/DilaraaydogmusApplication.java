package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
//redis icin:
@EnableCaching
public class DilaraaydogmusApplication {

    public static void main(String[] args) {
        SpringApplication.run(DilaraaydogmusApplication.class, args);
    }

}
