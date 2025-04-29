package com.bulish.melnikov.converter.config;

import org.springframework.boot.autoconfigure.amqp.ConnectionFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    ConnectionFactoryCustomizer customizer() {
        return cf -> cf.setMaxInboundMessageBodySize(1024 * 1024 * 128);
    }
}
