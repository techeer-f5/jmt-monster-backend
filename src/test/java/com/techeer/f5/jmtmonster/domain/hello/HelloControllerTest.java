package com.techeer.f5.jmtmonster.domain.hello;


import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.hello.controller.HelloController;
import com.techeer.f5.jmtmonster.domain.hello.dto.HelloRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HelloController.class)
@AutoConfigureRestDocs
// For use test db
@TestPropertySource("classpath:application-test.yml")
public class HelloControllerTest {
    // See https://spring.io/guides/gs/testing-restdocs/
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void checkGetApi() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/hello")
                            .accept(MediaType.APPLICATION_JSON)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(MockMvcRestDocumentationWrapper.document("hello-get",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .description("Hello 객체를 GET으로 가져옵니다.")
                                            .summary("Hello 객체 조회")
                                            .responseFields(
                                                    fieldWithPath("value").type(JsonFieldType.STRING).description("response message."),
                                                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("success flag."),
                                                    fieldWithPath("createdOn").type(JsonFieldType.STRING).description("response created date.")
                                            )
                                            .build()
                            )
                    ));
    }

    @Test
    public void checkPostApi() throws Exception {
        HelloRequestDto requestDto = HelloRequestDto.builder()
                                        .stringValue("example")
                                        .intValue(1L)
                                        .build();
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/hello")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                            .with(csrf()))
                .andDo(print())
                .andExpect(status().is(200))
                .andDo(document("hello-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("Hello 객체를 POST로 만듭니다.")
                                        .summary("Hello 객체 생성")
                                        .requestFields(
                                                fieldWithPath("stringValue").type(JsonFieldType.STRING).description("string value."),
                                                fieldWithPath("intValue").type(JsonFieldType.NUMBER).description("int value.")
                                        )
                                        .responseFields(
                                                fieldWithPath("value").type(JsonFieldType.STRING).description("response message."),
                                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("success flag."),
                                                fieldWithPath("createdOn").type(JsonFieldType.STRING).description("response created date.")
                                        )
                                        .build()
                        )
                ));
    }

}
