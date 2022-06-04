package com.techeer.f5.jmtmonster.domain.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.techeer.f5.jmtmonster.domain.friend.dto.mapper.S3Mapper;
import com.techeer.f5.jmtmonster.domain.review.dto.response.S3ResponseDto;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
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
    private S3ResponseDto givenResponseDto;

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
        mockMvc.perform(multipart("/api/v1/images")
                        .file("image",mockMultipartFile.getBytes())) // 변수 이름 맵핑 시켜줘야 함.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(givenURL));
    }
    
    @Test
    @DisplayName("")
    void deleteImagesTest() {
        // given
        
        // when
        
        // then
    }
    
    

}
















