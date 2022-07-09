package com.techeer.f5.jmtmonster.domain.home.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.home.dto.HomeRequestDto;
import com.techeer.f5.jmtmonster.domain.home.repository.HomeRepository;
import com.techeer.f5.jmtmonster.domain.oauth.domain.AuthProvider;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
// Flush DB after each test
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// For use test db
@ActiveProfiles(profiles = {"test"})
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    private User user;
    private PersistentToken persistentToken;
    private HomeRequestDto homeRequestDto;

    @BeforeEach
    public void setUp() {
        user = userRepository.build("이지호", "optional.int@kakao.com");
        persistentToken = persistentTokenRepository.build(user, AuthProvider.KAKAO);
        homeRequestDto = HomeRequestDto.builder()
                .code("123")
                .name("abc")
                .build();
    }

    @Test
    public void testGetHome() throws Exception {
        testMigrate();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/home")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                String.format("Bearer %s", persistentToken.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("get-home",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("현재 거주하고 있는 집을 조회합니다.")
                                        .summary("집 조회")
                                        .responseFields(
                                                fieldWithPath("name").type(
                                                        JsonFieldType.STRING).optional().description("행정구역 이름"),
                                                fieldWithPath("code").type(JsonFieldType.STRING)
                                                        .optional().description("행정구역 코드")
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    public void testGetHistory() throws Exception {
        testMigrate();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/home/history")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                String.format("Bearer %s", persistentToken.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("get-home-history",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("거주 이력을 조회합니다.")
                                        .summary("거주 이력 조회")
                                        .responseFields(
                                                fieldWithPath("history.[].name").type(
                                                        JsonFieldType.STRING).description("행정구역 이름"),
                                                fieldWithPath("history.[].code").type(JsonFieldType.STRING)
                                                        .description("행정구역 코드"),
                                                fieldWithPath("history.[].isCurrentHome").type(JsonFieldType.BOOLEAN)
                                                        .description("현재 거주중인 집인지 확인")
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    public void testMigrate() throws Exception {
        String requestBody = objectMapper.writeValueAsString(homeRequestDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/home")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION,
                                String.format("Bearer %s", persistentToken.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("put-migrate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("새로운 집으로 이사합니다.")
                                        .summary("이사 API")
                                        .requestFields(
                                                fieldWithPath("name").type(
                                                        JsonFieldType.STRING).description("행정구역 이름"),
                                                fieldWithPath("code").type(JsonFieldType.STRING)
                                                        .description("행정구역 코드")
                                        )
                                        .responseFields(
                                                fieldWithPath("name").type(
                                                        JsonFieldType.STRING).optional().description("행정구역 이름"),
                                                fieldWithPath("code").type(JsonFieldType.STRING).optional()
                                                        .description("행정구역 코드")
                                        )
                                        .build()
                        )
                ));
    }


}
