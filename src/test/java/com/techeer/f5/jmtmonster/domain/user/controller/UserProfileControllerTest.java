package com.techeer.f5.jmtmonster.domain.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.techeer.f5.jmtmonster.document.util.ResponseFieldDescriptorUtils.withPageDescriptorsIgnored;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.BasicUserResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import com.techeer.f5.jmtmonster.domain.user.service.UserService;
import com.techeer.f5.jmtmonster.util.FieldUtil;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserProfileController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ActiveProfiles(profiles = {"secret", "test", "disable-auth"})
@Import({UserMapper.class})
@DisplayName("사용자 프로필 API")
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("사용자 프로필 조회")
    class GetUserProfileTest {

        @Test
        @DisplayName("성공")
        void get_ok() throws Exception {
            User user = User.builder()
                    .name("Testuser")
                    .email("testuser@example.com")
                    .nickname("Testuser")
                    .imageUrl("https://profile.example.com/testuser")
                    .build();

            FieldUtil.writeField(user, "id", UUID.randomUUID());

            List<User> content = List.of(user);

            Page<User> pageResponse = new PageImpl<>(content, PageRequest.of(0, 10),
                    content.size());

            Page<BasicUserResponseDto> response = pageResponse.map(
                    userMapper::toBasicUserResponseDto);

            given(userService.searchByEmail(
                    any(),
                    eq(user.getEmail())
            )).willReturn(pageResponse);

            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("content.[].id")
                            .type(JsonFieldType.STRING)
                            .description("사용자 ID"),
                    fieldWithPath("content.[].name")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이름"),
                    fieldWithPath("content.[].email")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이메일"),
                    fieldWithPath("content.[].nickname")
                            .type(JsonFieldType.STRING)
                            .description("사용자 닉네임"),
                    fieldWithPath("content.[].imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("사용자 프로필 사진 주소")};

            mockMvc.perform(get("/api/v1/users")
                            .queryParam("email", user.getEmail())
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Origin", "*"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content()
                            .string(objectMapper.writeValueAsString(response)))
                    .andDo(document("user-profile-search",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("사용자 프로필을 이메일로 조회합니다.")
                                    .summary("사용자 프로필 조회")
                                    .requestParameters(
                                            parameterWithName("email")
                                                    .type(SimpleType.STRING)
                                                    .description("사용자 이메일")
                                                    .optional())
                                    .responseFields(
                                            withPageDescriptorsIgnored(responseFieldDescriptors))
                                    .build())));
        }
    }
}
