package com.techeer.f5.jmtmonster.domain.friend.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.friend.dto.mapper.FriendRequestMapper;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.friend.service.FriendService;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.BasicUserResponseDto;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = FriendRequestController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ActiveProfiles(profiles = {"secret", "test"})
@Import({FriendRequestMapper.class, UserMapper.class})
@DisplayName("친구 요청 API")
class FriendRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendService friendService;

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
                            .nickname("FromUser")
                            .email("test@jmt-monster.com")
                            .imageUrl("https://profile.example.com/from-user")
                            .build())
                    .toUser(User.builder()
                            .id(UUID.randomUUID())
                            .name("ToUser")
                            .nickname("ToUser")
                            .email("test@jmt-monster.com")
                            .imageUrl("https://profile.example.com/to-user")
                            .build())
                    .status(FriendRequestStatus.PENDING)
                    .build();

            FriendRequestResponseDto responseDto = FriendRequestResponseDto.builder()
                    .id(friendRequest.getId())
                    .fromUser(BasicUserResponseDto.builder()
                            .id(friendRequest.getFromUser().getId())
                            .name(friendRequest.getFromUser().getName())
                            .nickname(friendRequest.getFromUser().getNickname())
                            .email(friendRequest.getFromUser().getEmail())
                            .imageUrl(friendRequest.getFromUser().getImageUrl())
                            .build())
                    .toUser(BasicUserResponseDto.builder()
                            .id(friendRequest.getToUser().getId())
                            .name(friendRequest.getToUser().getName())
                            .nickname(friendRequest.getToUser().getNickname())
                            .email(friendRequest.getToUser().getEmail())
                            .imageUrl(friendRequest.getToUser().getImageUrl())
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
                                                    fieldWithPath("fromUser.id")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 보낸 사용자 ID"),
                                                    fieldWithPath("fromUser.name")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 보낸 사용자 이름"),
                                                    fieldWithPath("fromUser.email")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 보낸 사용자 이메일"),
                                                    fieldWithPath("fromUser.nickname")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 보낸 사용자 닉네임"),
                                                    fieldWithPath("fromUser.imageUrl")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 보낸 사용자 프로필 사진 주소"),
                                                    fieldWithPath("toUser.id")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 받은 사용자"),
                                                    fieldWithPath("toUser.name")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 받은 사용자 이름"),
                                                    fieldWithPath("toUser.email")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 받은 사용자 이메일"),
                                                    fieldWithPath("toUser.nickname")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 받은 사용자 닉네임"),
                                                    fieldWithPath("toUser.imageUrl")
                                                            .type(JsonFieldType.STRING)
                                                            .description("친구 요청을 받은 사용자 프로필 사진 주소"),
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

            FriendRequest actual = FriendRequest.builder()
                    .id(UUID.randomUUID())
                    .fromUser(User.builder()
                            .id(requestDto.getFromUserId())
                            .name("FromUser")
                            .nickname("FromUser")
                            .email("test@jmt-monster.com")
                            .imageUrl("https://profile.example.com/from-user")
                            .build())
                    .toUser(User.builder()
                            .id(requestDto.getToUserId())
                            .name("ToUser")
                            .nickname("ToUser")
                            .email("test@jmt-monster.com")
                            .imageUrl("https://profile.example.com/to-user")
                            .build())
                    .status(FriendRequestStatus.PENDING)
                    .build();

            given(friendService.createRequest(any(FriendRequestCreateServiceDto.class)))
                    .willReturn(actual);

            mockMvc.perform(post("/api/v1/friend-requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().is(201))
                    .andDo(document("friend-request-create",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("친구 요청 한 건을 생성합니다.")
                                    .summary("친구 요청 생성")
                                    .requestFields(
                                            fieldWithPath("fromUserId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 ID"),
                                            fieldWithPath("toUserId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자 ID")
                                    )
                                    .responseFields(
                                            fieldWithPath("id")
                                                    .type(JsonFieldType.STRING)
                                                    .description("ID"),
                                            fieldWithPath("fromUser.id")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 ID"),
                                            fieldWithPath("fromUser.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 이름"),
                                            fieldWithPath("fromUser.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 이메일"),
                                            fieldWithPath("fromUser.nickname")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 닉네임"),
                                            fieldWithPath("fromUser.imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 프로필 사진 주소"),
                                            fieldWithPath("toUser.id")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자"),
                                            fieldWithPath("toUser.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자 이름"),
                                            fieldWithPath("toUser.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자 이메일"),
                                            fieldWithPath("toUser.nickname")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자 닉네임"),
                                            fieldWithPath("toUser.imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자 프로필 사진 주소"),
                                            fieldWithPath("status")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상태"),
                                            subsectionWithPath("_links")
                                                    .ignored())
                                    .build()
                            )));
        }
    }

    @Nested
    @DisplayName("친구 요청으로부터 친구 생성")
    class UpdateFriendRequestToCreateFriendTest {

        @Test
        void updateFriendRequestToCreateFriend_ok() throws Exception {
            FriendRequestUpdateRequestDto requestDto = FriendRequestUpdateRequestDto.builder()
                    .status(FriendRequestStatus.ACCEPTED)
                    .build();

            UUID frId = UUID.randomUUID();
            FriendRequest actual = FriendRequest.builder()
                    .id(frId)
                    .fromUser(User.builder()
                            .id(UUID.randomUUID())
                            .name("FromUser")
                            .nickname("FromUser")
                            .email("test@jmt-monster.com")
                            .imageUrl("https://profile.example.com/from-user")
                            .build())
                    .toUser(User.builder()
                            .id(UUID.randomUUID())
                            .name("ToUser")
                            .nickname("ToUser")
                            .email("test@jmt-monster.com")
                            .imageUrl("https://profile.example.com/to-user")
                            .build())
                    .status(FriendRequestStatus.ACCEPTED)
                    .build();

            given(friendService.updateRequest(any(UUID.class),
                    any(FriendRequestUpdateServiceDto.class)))
                    .willReturn(actual);

            mockMvc.perform(put("/api/v1/friend-requests/{id}", frId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andDo(print())
                    .andExpect(status().is(200))
                    .andDo(document("friend-request-update-to-create-friend",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("친구 요청을 수정해 친구를 생성합니다.")
                                    .summary("친구 요청으로부터 친구 생성")
                                    .requestFields(
                                            fieldWithPath("status")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청 상태 (ACCEPTED)")
                                    )
                                    .responseFields(
                                            fieldWithPath("id")
                                                    .type(JsonFieldType.STRING)
                                                    .description("ID"),
                                            fieldWithPath("fromUser.id")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 ID"),
                                            fieldWithPath("fromUser.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 이름"),
                                            fieldWithPath("fromUser.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 이메일"),
                                            fieldWithPath("fromUser.nickname")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 닉네임"),
                                            fieldWithPath("fromUser.imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 보낸 사용자 프로필 사진 주소"),
                                            fieldWithPath("toUser.id")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자"),
                                            fieldWithPath("toUser.name")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자 이름"),
                                            fieldWithPath("toUser.email")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자 이메일"),
                                            fieldWithPath("toUser.nickname")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자 닉네임"),
                                            fieldWithPath("toUser.imageUrl")
                                                    .type(JsonFieldType.STRING)
                                                    .description("친구 요청을 받은 사용자 프로필 사진 주소"),
                                            fieldWithPath("status")
                                                    .type(JsonFieldType.STRING)
                                                    .description("상태"))
                                    .build()
                            )));
        }
    }
}
