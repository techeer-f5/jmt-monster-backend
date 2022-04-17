package com.techeer.f5.jmtmonster.global.config;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    /**
     * Bean to make jackson automatically convert from
     * camelCase (java) to under_scores (json) in property names
     *
     * @return ObjectMapper that maps from Java camelCase to json under_score names
     */
    @Bean
    public ObjectMapper jacksonObjectMapper()
    {
        return new ObjectMapper()
                    .setPropertyNamingStrategy(propertyNamingStrategy())
                    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public PropertyNamingStrategy propertyNamingStrategy()
    {
        return PropertyNamingStrategies.SNAKE_CASE;
    }


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(jacksonObjectMapper());
        messageConverters.add(jsonMessageConverter);

        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

}
