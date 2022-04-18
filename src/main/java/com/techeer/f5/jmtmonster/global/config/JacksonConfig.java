package com.techeer.f5.jmtmonster.global.config;


import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    @Qualifier("camelObjectMapper")
    public ObjectMapper camelObjectMapper() {
        return objectMapper(camelNamingStrategy());
    }

    @Bean
    @Qualifier("snakeObjectMapper")
    public ObjectMapper snakeObjectMapper() {
        return objectMapper(snakeNamingStrategy());
    }

    public ObjectMapper objectMapper(PropertyNamingStrategy propertyNamingStrategy) {
        return new ObjectMapper()
                .setPropertyNamingStrategy(propertyNamingStrategy)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    @Primary
    public PropertyNamingStrategy camelNamingStrategy()
    {
        return PropertyNamingStrategies.LOWER_CAMEL_CASE;
    }

    @Bean
    public PropertyNamingStrategy snakeNamingStrategy()
    {
        return PropertyNamingStrategies.SNAKE_CASE;
    }
}
