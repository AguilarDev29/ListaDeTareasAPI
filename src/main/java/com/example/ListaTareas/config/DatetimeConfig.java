package com.example.ListaTareas.config;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class DatetimeConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.timeZone(TimeZone.getTimeZone("America/Argentina_Buenos_Aires"));
            builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        };
    }
}
