package com.techeer.f5.jmtmonster.domain.review.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.review.service.S3Service;
import com.techeer.f5.jmtmonster.global.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = S3Controller.class)
@ContextConfiguration(classes = {SecurityConfig.class}) // 해당 클래스만 불러옴.
//@Import(SecurityConfig.class) -> 기존에 context에 securityconfig를 추가함.
public class S3ControllerTest {

    @Mock
    private S3Service s3Service;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private MultipartFile mockMultipartFile;
    private String expectedUrl;
    private String filename;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider){
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();

        expectedUrl = "TEST URL";
        filename = "mock.png";
        mockMultipartFile = new MockMultipartFile("file", filename,
                "image/png", "test data".getBytes());
    }

   @Test
    @DisplayName("이미지 업로드")
    void uploadImageTest() throws Exception {
        // given
        given(s3Service.uploadImage(mockMultipartFile)).willReturn(expectedUrl);

        // when

        // then
        mockMvc.perform(
                multipart("/api/v1/images")
                        .file("image",mockMultipartFile.getBytes())
                        .with(requestPostProcessor ->{
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedUrl))
                .andDo(print())
                .andDo(MockMvcRestDocumentationWrapper.document("/api/v1/images",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("이미지를 업로드 합니다.")
                                        .summary("이미지 업로드")
                                        .responseFields(
                                                fieldWithPath("url").type(JsonFieldType.STRING).description("이미지 URL")
                                        )
                                        .build()
                        )
                ))
                ;
    }

    @Test
    @DisplayName("이미지 삭제")
    void deleteByURLTest() {
        // given
        given(s3Service.deleteByURL(expectedUrl)).willReturn(expectedUrl);

        // when
        String actualUrl = s3Service.deleteByURL(expectedUrl);

        // then
    }

    @Test
    @DisplayName("이미지 업데이트트")
    void updateByURLTest() {
        // give

        // when

        // then
    }


}
