package com.techeer.f5.jmtmonster.domain.hello;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.techeer.f5.jmtmonster.domain.hello.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloController.class)
@AutoConfigureRestDocs(outputDir = "target/hello")
// For use test db
@TestPropertySource("classpath:application-test.yml")
public class HelloControllerTest {
    // See https://spring.io/guides/gs/testing-restdocs/
    @Autowired
    private MockMvc mockMvc;
    private String path = "/api/v1/hello";

    @Test
    public void checkGetApi() throws Exception {

        mockMvc.perform(get(path).accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                .andDo(document("get", responseFields(
                        fieldWithPath("value").type(JsonFieldType.STRING).description("response message."),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("success flag."),
                        fieldWithPath("createdOn").type(JsonFieldType.STRING).description("response created date.")
                )));
    }

    @Test
    public void checkPostApi() throws Exception {

        mockMvc.perform(post(path).accept(MediaType.APPLICATION_JSON)
                        .param("stringValue", "example")
                        .param("intValue", String.valueOf(1)))
                .andExpect(status().isCreated())
                .andDo(document("create", responseFields(
                        fieldWithPath("value").type(JsonFieldType.STRING).description("response message."),
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("success flag."),
                        fieldWithPath("createdOn").type(JsonFieldType.STRING).description("response created date.")
                )));
    }

}
