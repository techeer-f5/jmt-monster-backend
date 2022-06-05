package com.techeer.f5.jmtmonster.domain.review.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.review.dto.mapper.S3Mapper;
import com.techeer.f5.jmtmonster.domain.review.dto.response.S3UploadResponseDto;
import com.techeer.f5.jmtmonster.domain.review.service.S3Service;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(S3Controller.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ActiveProfiles(profiles = {"test","secret","disable-auth"})
@Import({S3Mapper.class, UserMapper.class})
@DisplayName("S3 API")
public class S3ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private S3Service s3Service;

    @Autowired
    private S3Mapper mapper;

    private String givenURL;
    private String givenFileName;
    private MockMultipartFile mockMultipartFile;
    private String file;
    private S3UploadResponseDto givenResponseDto;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) throws IOException {
        file = "mock1.png";
        mockMultipartFile = new MockMultipartFile("file", file,
                "image/png", "test data".getBytes());
        givenURL = "Test URL";
        givenFileName = "nanoidabcd-mock1.png";
        givenResponseDto.builder()
                .url(givenURL)
                .build();
        given(s3Service.uploadImage(any())).willReturn(givenURL);
        given(s3Service.deleteImage(any())).willReturn(givenFileName);
        given(s3Service.deleteAndCreateImage(any(),any())).willReturn(givenURL);
    }
    
    @Test
    @DisplayName("이미지 업로드 테스트")
    void uploadImage() throws Exception {
        // given
        
        // when
        
        // then
        FieldDescriptor[] responseFieldDescriptors = {
                fieldWithPath("content.[].url")
                        .type(JsonFieldType.STRING)
                        .description("URL of image in S3")};

        mockMvc.perform(multipart("/api/v1/images")
                        .file("image",mockMultipartFile.getBytes())) // 변수 이름 맵핑 시켜줘야 함.
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(givenURL))
                .andDo(document("upload image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                            partWithName("image").description("The image to upload to s3")),
                        resource(ResourceSnippetParameters.builder()
                                .description("이미지를 하나 S3에 업로드합니다..")
                                .summary("S3 이미지 업로드")
                                .responseFields(responseFieldDescriptors)
                                .build())));
    }
    
    @Test
    @DisplayName("")
    void deleteImagesTest() throws Exception {
        // given
        
        // when
        
        // then
        FieldDescriptor[] responseFieldDescriptors = {
                fieldWithPath("content.[].filename")
                        .type(JsonFieldType.STRING)
                        .description("File name of image in S3")};

        mockMvc.perform(get("/api/v1/images")
                .param("filename",givenFileName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filename").value(givenFileName))
                .andDo(document("delete image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("The image to upload to s3")),
                        resource(ResourceSnippetParameters.builder()
                                .description("이미지를 하나 S3에서 삭제 합니다. 파일을 삭제할 땐 '폴더명/나노아이디-파일이름.확장자' 형식의 이름을 사용할 것.")
                                .summary("S3 이미지 삭제")
                                .responseFields(responseFieldDescriptors)
                                .build())));
    }
    
    

}
















