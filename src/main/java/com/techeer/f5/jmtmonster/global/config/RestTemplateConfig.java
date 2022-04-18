package com.techeer.f5.jmtmonster.global.config;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Autowired
    @Qualifier("camelObjectMapper")
    private ObjectMapper camelObjectMapper;

    @Autowired
    @Qualifier("snakeObjectMapper")
    private ObjectMapper snakeObjectMapper;

    @Bean
    @Primary
    @Qualifier("camelRestTemplate")
    public RestTemplate camelRestTemplate() {
        return createRestTemplate(camelObjectMapper);
    }

    @Bean
    @Qualifier("snakeRestTemplate")
    public RestTemplate snakeRestTemplate() {
        return createRestTemplate(snakeObjectMapper);
    }


    public RestTemplate createRestTemplate(ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(objectMapper);
        messageConverters.add(jsonMessageConverter);

        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

}
