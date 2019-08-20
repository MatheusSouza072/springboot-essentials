package com.br.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootEssentialApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootEssentialApplication.class, args);
    }

    @Test
    public void contextLoads()
    {

    }}
