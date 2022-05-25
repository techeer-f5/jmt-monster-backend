package com.techeer.f5.jmtmonster.domain.hello;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.hello.dto.HelloRequestDto;
import com.techeer.f5.jmtmonster.global.config.JacksonConfig;
import com.techeer.f5.jmtmonster.global.config.JacksonModuleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
// For use test db
@ActiveProfiles(profiles = {"test", "disable-auth"})
@Import({JacksonConfig.class, JacksonModuleConfig.class})
public class HelloControllerTest {
    // See https://spring.io/guides/gs/testing-restdocs/
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("camelObjectMapper")
    private ObjectMapper objectMapper;

    @Test
    public void checkGetApi() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/hello")
                            .accept(MediaType.APPLICATION_JSON))
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
                                                    fieldWithPath("value").type(JsonFieldType.STRING).description("리스폰스 메시지"),
                                                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부를 나타내는 불린 변수"),
                                                    fieldWithPath("createdOn").type(JsonFieldType.STRING).description("객체 생성 시각")
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
                            .content(requestBody))
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
                                                fieldWithPath("stringValue").type(JsonFieldType.STRING).description("문자열 값"),
                                                fieldWithPath("intValue").type(JsonFieldType.NUMBER).description("정수 값")
                                        )
                                        .responseFields(
                                                fieldWithPath("value").type(JsonFieldType.STRING).description("리스폰스 메시지"),
                                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부를 나타내는 불린 변수"),
                                                fieldWithPath("createdOn").type(JsonFieldType.STRING).description("객체 생성 시각")
                                        )
                                        .build()
                        )
                ));
    }

}
