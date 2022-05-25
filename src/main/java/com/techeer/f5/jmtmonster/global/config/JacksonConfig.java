package com.techeer.f5.jmtmonster.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.techeer.f5.jmtmonster.global.builder.ObjectMapperBuilder;
import lombok.RequiredArgsConstructor;
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
        return ObjectMapperBuilder.simpleBuild(camelNamingStrategy(), pageJacksonModule,
                sortJacksonModule);
    }

    @Bean
    @Qualifier("snakeObjectMapper")
    public ObjectMapper snakeObjectMapper() {
        return ObjectMapperBuilder.simpleBuild(snakeNamingStrategy(), pageJacksonModule,
                sortJacksonModule);
    }

    @Bean
    @Primary
    public PropertyNamingStrategy camelNamingStrategy() {
        return new CamelNamingStrategy();
    }

    @Bean
    public PropertyNamingStrategy snakeNamingStrategy() {
        return new SnakeNamingStrategy();
    }

    public static class CamelNamingStrategy extends
            PropertyNamingStrategies.LowerCamelCaseStrategy {

        @Override
        public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method,
                String defaultName) {
            if (method.getParameterCount() == 1 &&
                    (method.getRawParameterType(0) == Boolean.class
                            || method.getRawParameterType(0) == boolean.class) &&
                    method.getName().startsWith("set")) {

                Class<?> containingClass = method.getDeclaringClass();
                String potentialFieldName = "is" + method.getName().substring(3);

                try {
                    containingClass.getDeclaredField(potentialFieldName);
                    return potentialFieldName;
                } catch (NoSuchFieldException e) {
                    // do nothing and fall through
                }
            }

            return super.nameForSetterMethod(config, method, defaultName);
        }

        @Override
        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method,
                String defaultName) {
            if (method.hasReturnType() && (method.getRawReturnType() == Boolean.class
                    || method.getRawReturnType() == boolean.class)
                    && method.getName().startsWith("is")) {

                Class<?> containingClass = method.getDeclaringClass();
                String potentialFieldName = method.getName();

                try {
                    containingClass.getDeclaredField(potentialFieldName);
                    return potentialFieldName;
                } catch (NoSuchFieldException e) {
                    // do nothing and fall through
                }
            }
            return super.nameForGetterMethod(config, method, defaultName);
        }


    }

    public static class SnakeNamingStrategy extends PropertyNamingStrategies.SnakeCaseStrategy {

        @Override
        public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method,
                String defaultName) {
            if (method.getParameterCount() == 1 &&
                    (method.getRawParameterType(0) == Boolean.class
                            || method.getRawParameterType(0) == boolean.class) &&
                    method.getName().startsWith("set")) {

                Class<?> containingClass = method.getDeclaringClass();
                String potentialFieldName = "is" + method.getName().substring(3);

                try {
                    containingClass.getDeclaredField(potentialFieldName);
                    return potentialFieldName;
                } catch (NoSuchFieldException e) {
                    // do nothing and fall through
                }
            }

            return super.nameForSetterMethod(config, method, defaultName);
        }

        @Override
        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method,
                String defaultName) {
            if (method.hasReturnType() && (method.getRawReturnType() == Boolean.class
                    || method.getRawReturnType() == boolean.class)
                    && method.getName().startsWith("is")) {

                Class<?> containingClass = method.getDeclaringClass();
                String potentialFieldName = method.getName();

                try {
                    containingClass.getDeclaredField(potentialFieldName);
                    return potentialFieldName;
                } catch (NoSuchFieldException e) {
                    // do nothing and fall through
                }
            }
            return super.nameForGetterMethod(config, method, defaultName);
        }
    }
}
