package com.techeer.f5.jmtmonster.global.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectMapperFactory {

    public static ObjectMapper create(
            PropertyNamingStrategy propertyNamingStrategy,
            List<Module> modules,
            List<DeserializationFeature> configures,
            List<SerializationFeature> disableSerializationFeatures) {
        ObjectMapper result = new ObjectMapper();

        result = result.setPropertyNamingStrategy(propertyNamingStrategy);

        if (modules != null) {
            for (var module : modules) {
                result = result.registerModule(module);
            }
        }

        if (configures != null) {
            for (var feature : configures) {
                result = result.configure(feature, true);
            }
        }

        if (disableSerializationFeatures != null) {
            for (var disableSerializationFeature : disableSerializationFeatures) {
                result = result.disable(disableSerializationFeature);
            }
        }

        return result;
    }

    public static ObjectMapper create(PropertyNamingStrategy propertyNamingStrategy,
            Module... modules) {
        return createWithDefaults(propertyNamingStrategy, List.of(modules), List.of(), List.of());
    }

    public static ObjectMapper createWithDefaults(
            PropertyNamingStrategy propertyNamingStrategy,
            List<Module> modules,
            List<DeserializationFeature> configures,
            List<SerializationFeature> disableSerializationFeatures) {
        // 빌더가 인자로 List 클래스를 가질 경우, add 메소드 사용이 불가능함 (UnsupportedOperationError)
        // 간단하게 ArrayList로 변환시키는 작업 필요
        List<Module> modulesToAdd = new ArrayList<>();
        modulesToAdd.add(new JavaTimeModule());
        if (modules != null) {
            modulesToAdd.addAll(modules);
        }

        List<DeserializationFeature> configuresToAdd = new ArrayList<>();
        configuresToAdd.add(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        if (configures != null) {
            configuresToAdd.addAll(configures);
        }

        List<SerializationFeature> disableSerializationFeaturesToAdd = new ArrayList<>();
        disableSerializationFeaturesToAdd.add(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        if (disableSerializationFeatures != null) {
            disableSerializationFeaturesToAdd.addAll(disableSerializationFeatures);
        }

        return create(propertyNamingStrategy, modulesToAdd, configuresToAdd,
                disableSerializationFeaturesToAdd);
    }
}
