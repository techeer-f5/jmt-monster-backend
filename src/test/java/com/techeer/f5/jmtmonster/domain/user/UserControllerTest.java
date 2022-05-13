package com.techeer.f5.jmtmonster.domain.user;

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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @Transactional
    public void testMyUser() throws Exception {
        getMyUser();
    }

    public User getMyUser() throws Exception {
        User user = userRepository.build("이지호", "optional.int@kakao.com");

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
                                        .description("현재 사용자를 가져옵니다.")
                                        .summary("현재 사용자 조회")
                                        .requestHeaders(
                                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 사용자 토큰")
                                        )
                                        .responseFields(
                                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                fieldWithPath("user.id").type(JsonFieldType.STRING).description("사용자 ID"),
                                                fieldWithPath("user.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                                fieldWithPath("user.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                                fieldWithPath("user.nickname").type(JsonFieldType.STRING).optional().description("사용자 닉네임"),
                                                fieldWithPath("user.address").type(JsonFieldType.STRING).optional().description("사용자 주소"),
                                                fieldWithPath("user.imageUrl").type(JsonFieldType.STRING).optional().description("사용자 이미지 URL"),
                                                fieldWithPath("user.emailVerified").type(JsonFieldType.BOOLEAN).description("사용자 이메일 인증 여부"),
                                                fieldWithPath("user.extraInfoInjected").type(JsonFieldType.BOOLEAN).description("사용자 추가 정보 입력 여부"),
                                                fieldWithPath("user.verified").type(JsonFieldType.BOOLEAN).description("사용자 인증 여부 (emailVerified && extraInfoInjected)")
                                        )
                                        .build()
                        )
                ))
                .andReturn();

        UserResponseDto userResponseDto = JsonMapper.fromMvcResult(mvcResult, UserResponseDto.class);

        assertThat(userResponseDto.isSuccess()).isTrue();
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
                .address("경기도 시흥시 서울대학로278번길 19-13")
                .imageUrl(null)
                .build();

        MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.post("/users/me/extra-info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + persistentToken.getId().toString())
                        .content(JsonMapper.asJsonString(extraUserInfoRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("submit-extra-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .description("추가 정보를 입력합니다.")
                                        .summary("추가 정보 입력")
                                        .requestHeaders(
                                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 사용자 토큰")
                                        )
                                        .responseFields(
                                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                fieldWithPath("user.id").type(JsonFieldType.STRING).description("사용자 ID"),
                                                fieldWithPath("user.name").type(JsonFieldType.STRING).description("사용자 이름"),
                                                fieldWithPath("user.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                                fieldWithPath("user.nickname").type(JsonFieldType.STRING).optional().description("사용자 닉네임"),
                                                fieldWithPath("user.address").type(JsonFieldType.STRING).optional().description("사용자 주소"),
                                                fieldWithPath("user.imageUrl").type(JsonFieldType.STRING).optional().description("사용자 이미지 URL"),
                                                fieldWithPath("user.emailVerified").type(JsonFieldType.BOOLEAN).description("사용자 이메일 인증 여부"),
                                                fieldWithPath("user.extraInfoInjected").type(JsonFieldType.BOOLEAN).description("사용자 추가 정보 입력 여부"),
                                                fieldWithPath("user.verified").type(JsonFieldType.BOOLEAN).description("사용자 인증 여부 (emailVerified && extraInfoInjected)")
                                        )
                                        .build()
                        )
                ))
                .andReturn();

        UserResponseDto userResponseDto = JsonMapper.fromMvcResult(mvcResult, UserResponseDto.class);

        assertThat(userResponseDto.isSuccess()).isTrue();
        assertThat(userResponseDto.getUser()).isNotNull();

        assertThat(user.getName()).isEqualTo(userResponseDto.getUser().getName());
        assertThat(user.getEmail()).isEqualTo(userResponseDto.getUser().getEmail());
        assertThat(userResponseDto.getUser().getNickname()).isEqualTo("DPS0340");
        assertThat(userResponseDto.getUser().getAddress()).isEqualTo("경기도 시흥시 서울대학로278번길 19-13");
        assertThat(userResponseDto.getUser().getImageUrl()).isNull();
    }

}
