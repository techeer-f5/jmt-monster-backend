package com.techeer.f5.jmtmonster.domain.user;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.oauth.domain.AuthProvider;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.ExtraUserInfoRequestDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserResponseDto;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.domain.user.service.UserService;
import com.techeer.f5.jmtmonster.global.utils.JsonMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
// For use test db
@ActiveProfiles(profiles = {"test"})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    @Transactional
    public void testMyUser() throws Exception {
        getMyUser();
    }

    public User getMyUser() throws Exception {
        User user = userRepository.build("?????????", "optional.int@kakao.com");

        PersistentToken persistentToken = persistentTokenRepository.build(user, AuthProvider.KAKAO);

        MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.get("/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + persistentToken.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(MockMvcRestDocumentationWrapper.document("my-user",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .description("?????? ???????????? ???????????????.")
                        .summary("?????? ????????? ??????")
                        .requestHeaders(
                            headerWithName(
                                HttpHeaders.AUTHORIZATION).description(
                                "Bearer ????????? ??????")
                        )
                        .responseFields(
                            fieldWithPath("isSuccess").type(
                                JsonFieldType.BOOLEAN).description("?????? ??????"),
                            fieldWithPath("user.id").type(JsonFieldType.STRING)
                                .description("????????? ID"),
                            fieldWithPath("user.name").type(
                                JsonFieldType.STRING).description("????????? ??????"),
                            fieldWithPath("user.email").type(
                                    JsonFieldType.STRING)
                                .description("????????? ?????????"),
                            fieldWithPath("user.nickname").type(
                                    JsonFieldType.STRING).optional()
                                .description("????????? ?????????"),
                            fieldWithPath("user.address").type(
                                    JsonFieldType.STRING).optional()
                                .description("????????? ??????"),
                            fieldWithPath("user.imageUrl").type(
                                    JsonFieldType.STRING).optional()
                                .description("????????? ????????? URL"),
                            fieldWithPath("user.emailVerified").type(
                                    JsonFieldType.BOOLEAN)
                                .description("????????? ????????? ?????? ??????"),
                            fieldWithPath("user.extraInfoInjected").type(
                                    JsonFieldType.BOOLEAN)
                                .description("????????? ?????? ?????? ?????? ??????"),
                            fieldWithPath("user.verified").type(
                                JsonFieldType.BOOLEAN).description(
                                "????????? ?????? ?????? (emailVerified && extraInfoInjected)")
                        )
                        .build()
                )
            ))
            .andReturn();

        UserResponseDto userResponseDto = jsonMapper.fromMvcResult(mvcResult,
            UserResponseDto.class);

        assertThat(userResponseDto.getIsSuccess()).isTrue();
        assertThat(userResponseDto.getUser()).isNotNull();

        assertThat(user.getName()).isEqualTo(userResponseDto.getUser().getName());
        assertThat(user.getEmail()).isEqualTo(userResponseDto.getUser().getEmail());

        return user;
    }

    @Test
    @Transactional
    public void testSubmitExtraInfo() throws Exception {
        User user = getMyUser();
        PersistentToken persistentToken = user.getTokens().stream().findFirst().get();

        assertThat(user.getExtraInfoInjected()).isFalse();
        assertThat(user.getEmailVerified()).isFalse();
        assertThat(user.getVerified()).isFalse();

        ExtraUserInfoRequestDto extraUserInfoRequestDto = ExtraUserInfoRequestDto.builder()
            .nickname("DPS0340")
            .address("????????? ????????? ???????????????278?????? 19-13")
            .imageUrl(null)
            .build();

        MvcResult mvcResult = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/users/me/extra-info")
                    .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + persistentToken.getId().toString())
                    .content(jsonMapper.asJsonString(extraUserInfoRequestDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(MockMvcRestDocumentationWrapper.document("submit-extra-info",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .description("?????? ????????? ???????????????.")
                        .summary("?????? ?????? ??????")
                        .requestHeaders(
                            headerWithName(
                                HttpHeaders.AUTHORIZATION).description(
                                "Bearer ????????? ??????")
                        )
                        .responseFields(
                            fieldWithPath("isSuccess").type(
                                JsonFieldType.BOOLEAN).description("?????? ??????"),
                            fieldWithPath("user.id").type(JsonFieldType.STRING)
                                .description("????????? ID"),
                            fieldWithPath("user.name").type(
                                JsonFieldType.STRING).description("????????? ??????"),
                            fieldWithPath("user.email").type(
                                    JsonFieldType.STRING)
                                .description("????????? ?????????"),
                            fieldWithPath("user.nickname").type(
                                    JsonFieldType.STRING).optional()
                                .description("????????? ?????????"),
                            fieldWithPath("user.address").type(
                                    JsonFieldType.STRING).optional()
                                .description("????????? ??????"),
                            fieldWithPath("user.imageUrl").type(
                                    JsonFieldType.STRING).optional()
                                .description("????????? ????????? URL"),
                            fieldWithPath("user.emailVerified").type(
                                    JsonFieldType.BOOLEAN)
                                .description("????????? ????????? ?????? ??????"),
                            fieldWithPath("user.extraInfoInjected").type(
                                    JsonFieldType.BOOLEAN)
                                .description("????????? ?????? ?????? ?????? ??????"),
                            fieldWithPath("user.verified").type(
                                JsonFieldType.BOOLEAN).description(
                                "????????? ?????? ?????? (emailVerified && extraInfoInjected)")
                        )
                        .build()
                )
            ))
            .andReturn();

        UserResponseDto userResponseDto = jsonMapper.fromMvcResult(mvcResult,
            UserResponseDto.class);

        assertThat(userResponseDto.getIsSuccess()).isTrue();
        assertThat(userResponseDto.getUser()).isNotNull();

        assertThat(user.getName()).isEqualTo(userResponseDto.getUser().getName());
        assertThat(user.getEmail()).isEqualTo(userResponseDto.getUser().getEmail());
        assertThat(userResponseDto.getUser().getNickname()).isEqualTo("DPS0340");
        assertThat(userResponseDto.getUser().getImageUrl()).isNull();
    }

    @Test
    @DisplayName("???????????? ?????? ???????????? /users/me API ?????? ??? 401")
    public void testMyInfoWithNotRegisteredToken() throws Exception {
        UUID dummyToken = UUID.randomUUID();
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/users/me")
                    .header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + dummyToken.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    @DisplayName("?????? ?????? /users/me API ?????? ??? 401")
    public void testMyInfoWithoutAuthorization() throws Exception {
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/users/me")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

}
