package com.techeer.f5.jmtmonster.global.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

@Component
public class JsonMapper {

    @Autowired
    @Qualifier("camelObjectMapper")
    public ObjectMapper objectMapper;

    public String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromMvcResult(MvcResult result, Class<T> clazz) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }

    public <T> T fromMvcResult(MvcResult result, TypeReference<T> typeReference) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
    }
}
