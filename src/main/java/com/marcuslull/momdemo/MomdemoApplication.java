package com.marcuslull.momdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:secrets.properties")
public class MomdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomdemoApplication.class, args);
    }

}
