package com.techeer.f5.jmtmonster.domain.friend.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.friend.dto.mapper.FriendRequestMapper;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.friend.service.FriendService;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.BasicUserResponseDto;
import com.techeer.f5.jmtmonster.global.config.SecurityConfig;
import com.techeer.f5.jmtmonster.security.extractor.AuthorizationExtractor;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@WebMvcTest(FriendRequestController.class)
//@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {SecurityConfig.class})
@AutoConfigureRestDocs
@ActiveProfiles(profiles = {"secret", "test"})
@DisplayName("친구 API")
class FriendRequestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendService friendService;

    @MockBean
    private FriendRequestMapper friendRequestMapper;

    @Nested
    @DisplayName("친구 요청 조회")
    class GetFriendshipTest {

        @Test
        void getFriendRequest_ok() throws Exception {

            FriendRequest friendRequest = FriendRequest.builder()
                    .id(UUID.randomUUID())
                    .fromUser(User.builder()
                            .id(UUID.randomUUID())
                            .name("FromUser")
                            .email("test@jmt-monster.com")
                            .build())
                    .toUser(User.builder()
                            .id(UUID.randomUUID())
                            .name("ToUser")
                            .email("test@jmt-monster.com")
                            .build())
                    .status(FriendRequestStatus.PENDING)
                    .build();

            FriendRequestResponseDto responseDto = FriendRequestResponseDto.builder()
                    .id(friendRequest.getId())
                    .fromUser(BasicUserResponseDto.builder()
                            .id(friendRequest.getFromUser().getId())
                            .name(friendRequest.getFromUser().getName())
                            .email(friendRequest.getFromUser().getEmail())
                            .build())
                    .toUser(BasicUserResponseDto.builder()
                            .id(friendRequest.getToUser().getId())
                            .name(friendRequest.getToUser().getName())
                            .email(friendRequest.getToUser().getEmail())
                            .build())
                    .status(FriendRequestStatus.PENDING)
                    .build();

            given(friendService.findRequestById(any()))
                    .willReturn(friendRequest);

            mockMvc.perform(get("/api/v1/friend-requests/" + friendRequest.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Origin", "*")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(responseDto)))
                    .andDo(document("friend-request-get-one",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .description("친구 요청 한 건을 조회합니다.")
                                            .summary("친구 요청 조회")
                                            .responseFields(
                                                    fieldWithPath("id")
                                                            .type(JsonFieldType.STRING)
                                                            .description("ID"),
                                                    fieldWithPath("fromUser")
                                                            .type(JsonFieldType.OBJECT)
                                                            .description("친구 요청을 보낸 사용자"),
                                                    fieldWithPath("toUser")
                                                            .type(JsonFieldType.OBJECT)
                                                            .description("친구 요청을 받은 사용자"),
                                                    fieldWithPath("status")
                                                            .type(JsonFieldType.STRING)
                                                            .description("상태")
                                            )
                                            .build()
                            )
                    ));
        }
    }

    @Nested
    @DisplayName("친구 요청 생성")
    class CreateFriendRequestTest {

        @Test
        void createFriendRequest_ok() throws Exception {

            FriendRequestCreateRequestDto requestDto = FriendRequestCreateRequestDto.builder()
                    .fromUserId(UUID.randomUUID())
                    .toUserId(UUID.randomUUID())
                    .build();

            String requestBody = objectMapper.writeValueAsString(requestDto);

            mockMvc.perform(post("/api/v1/friend-requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().is(201))
                    .andDo(document("friend-request-create",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(
                                    ResourceSnippetParameters.builder()
                                            .description("Hello 객체를 POST로 만듭니다.")
                                            .summary("Hello 객체 생성")
                                            .requestFields(
                                                    fieldWithPath("stringValue")
                                                            .type(JsonFieldType.STRING)
                                                            .description("문자열 값"),
                                                    fieldWithPath("intValue")
                                                            .type(JsonFieldType.NUMBER)
                                                            .description("정수 값")
                                            )
                                            .responseFields(
                                                    fieldWithPath("value")
                                                            .type(JsonFieldType.STRING)
                                                            .description("리스폰스 메시지"),
                                                    fieldWithPath("success")
                                                            .type(JsonFieldType.BOOLEAN)
                                                            .description("성공 여부를 나타내는 불린 변수"),
                                                    fieldWithPath("createdOn")
                                                            .type(JsonFieldType.STRING)
                                                            .description("객체 생성 시각")
                                            )
                                            .build()
                            )
                    ));
        }
    }
}
