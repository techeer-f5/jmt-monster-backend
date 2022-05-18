package com.techeer.f5.jmtmonster.global.config;


import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class JacksonConfig {
    public final PageJacksonModule pageJacksonModule;
    public final SortJacksonModule sortJacksonModule;

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
                .registerModule(pageJacksonModule)
                .registerModule(sortJacksonModule)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    @Primary
    public PropertyNamingStrategy camelNamingStrategy()
    {
        return new CamelNamingStrategy();
    }

    @Bean
    public PropertyNamingStrategy snakeNamingStrategy()
    {
        return new SnakeNamingStrategy();
    }

    public static class CamelNamingStrategy extends PropertyNamingStrategies.LowerCamelCaseStrategy {
        @Override
        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName)
        {
            if(method.hasReturnType() && (method.getRawReturnType() == Boolean.class || method.getRawReturnType() == boolean.class)
                    && method.getName().startsWith("is")) {
                return method.getName();
            }
            return super.nameForGetterMethod(config, method, defaultName);
        }


    }

    public static class SnakeNamingStrategy extends PropertyNamingStrategies.SnakeCaseStrategy {
        @Override
        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName)
        {
            if(method.hasReturnType() && (method.getRawReturnType() == Boolean.class || method.getRawReturnType() == boolean.class)
                    && method.getName().startsWith("is")) {
                return method.getName();
            }
            return super.nameForGetterMethod(config, method, defaultName);
        }
    }
}
