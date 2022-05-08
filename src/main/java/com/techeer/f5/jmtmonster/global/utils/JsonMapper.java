package com.techeer.f5.jmtmonster.global.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

@Component
public class JsonMapper {
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromMvcResult(MvcResult result, Class<T> clazz) throws Exception {
        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), clazz);
    }

}