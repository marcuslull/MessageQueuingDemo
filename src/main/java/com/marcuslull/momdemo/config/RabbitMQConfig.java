package com.marcuslull.momdemo.config;

import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConfig {

    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        String PACKAGE_NAME = "*";
        converter.setAllowedListPatterns(List.of(PACKAGE_NAME)); // required by RabbitMQ as a security measure
        return converter;
    }
}
