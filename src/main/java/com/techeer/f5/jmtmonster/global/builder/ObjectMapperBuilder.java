package com.techeer.f5.jmtmonster.global.builder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;

@Builder
public class ObjectMapperBuilder {

    private final PropertyNamingStrategy propertyNamingStrategy;

    @Builder.Default
    private List<Module> modules = new ArrayList<>();

    @Builder.Default
    private List<DeserializationFeature> configures = new ArrayList<>();

    @Builder.Default
    private List<SerializationFeature> disableSerializationFeatures = new ArrayList<>();

    public ObjectMapper build() {
        ObjectMapper result = new ObjectMapper();

        if (propertyNamingStrategy != null) {
            result = result.setPropertyNamingStrategy(propertyNamingStrategy);
        }

        for (var feature : configures) {
            result = result.configure(feature, true);
        }

        for (var disableSerializationFeature : disableSerializationFeatures) {
            result = result.disable(disableSerializationFeature);
        }

        for (var module : modules) {
            result = result.registerModule(module);
        }

        return result;
    }

    public ObjectMapper buildWithDefaults() {
        // 빌더가 인자로 List 클래스를 가질 경우, add 메소드 사용이 불가능함 (UnsupportedOperationError)
        // 간단하게 ArrayList로 변환시키는 작업
        modules = new ArrayList<>(modules);
        configures = new ArrayList<>(configures);
        disableSerializationFeatures = new ArrayList<>(disableSerializationFeatures);

        modules.add(new JavaTimeModule());
        configures.add(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        disableSerializationFeatures.add(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return build();
    }

    public static ObjectMapper simpleBuild(PropertyNamingStrategy propertyNamingStrategy,
            Module... modules) {
        return builder()
                .propertyNamingStrategy(propertyNamingStrategy)
                .modules(Arrays.stream(modules).toList())
                .build()
                .buildWithDefaults();
    }
}
