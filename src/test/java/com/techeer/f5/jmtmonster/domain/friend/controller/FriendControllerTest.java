package com.techeer.f5.jmtmonster.domain.friend.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.techeer.f5.jmtmonster.document.util.ResponseFieldDescriptorUtils.withPageDescriptorsIgnored;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
import com.techeer.f5.jmtmonster.domain.friend.dto.mapper.FriendMapper;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendHangOutDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendResponseDto;
import com.techeer.f5.jmtmonster.domain.friend.service.FriendService;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
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


@WebMvcTest(FriendController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ActiveProfiles(profiles = {"secret", "test", "disable-auth"})
@Import({FriendMapper.class, UserMapper.class})
@DisplayName("친구 API")
class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FriendMapper friendMapper;

    @MockBean
    private FriendService friendService;

    @Nested
    @DisplayName("친구 단일 조회")
    class GetFriendTest {

        @Test
        @DisplayName("성공")
        void getFriend_ok() throws Exception {
            Friend friend = Friend.builder()
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
                    .isHangingOut(false)
                    .build();

            FieldUtil.writeField(friend, "id", UUID.randomUUID());

            FriendResponseDto responseDto = friendMapper.toResponseDto(friend);

            given(friendService.findFriendById(any()))
                    .willReturn(friend);

            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("id")
                            .type(JsonFieldType.STRING)
                            .description("ID"),
                    fieldWithPath("fromUser.id")
                            .type(JsonFieldType.STRING)
                            .description("사용자 ID"),
                    fieldWithPath("fromUser.name")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이름"),
                    fieldWithPath("fromUser.email")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이메일"),
                    fieldWithPath("fromUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("사용자 닉네임"),
                    fieldWithPath("fromUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("사용자 프로필 사진 주소"),
                    fieldWithPath("toUser.id")
                            .type(JsonFieldType.STRING)
                            .description("친구 ID"),
                    fieldWithPath("toUser.name")
                            .type(JsonFieldType.STRING)
                            .description("친구 이름"),
                    fieldWithPath("toUser.email")
                            .type(JsonFieldType.STRING)
                            .description("친구 이메일"),
                    fieldWithPath("toUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("친구 닉네임"),
                    fieldWithPath("toUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("친구 프로필 사진 주소"),
                    fieldWithPath("isHangingOut")
                            .type(JsonFieldType.BOOLEAN)
                            .description("놀러가기 여부")};

            mockMvc.perform(get("/api/v1/friends/{id}", friend.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Origin", "*"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(responseDto)))
                    .andDo(document("friend-get-one",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("친구 한 건을 조회합니다.")
                                    .summary("친구 조회")
                                    .responseFields(responseFieldDescriptors)
                                    .build())));
        }
    }

    @Nested
    @DisplayName("친구 목록 검색")
    class GetFriendListTest {

        @Test
        @DisplayName("전체 쿼리 사용 - 성공")
        void getFriendList_ok() throws Exception {
            Friend friend = Friend.builder()
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
                    .isHangingOut(false)
                    .build();

            FieldUtil.writeField(friend, "id", UUID.randomUUID());

            List<Friend> content = List.of(friend);

            Page<Friend> pageResponse = new PageImpl<>(content, PageRequest.of(0, 10),
                    content.size());

            Page<FriendResponseDto> response = pageResponse.map(friendMapper::toResponseDto);

            given(friendService.findAllFriends(
                    any(),
                    eq(friend.getFromUser().getId()),
                    eq(friend.getToUser().getId()),
                    eq(friend.isHangingOut())
            )).willReturn(pageResponse);

            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("content.[].id")
                            .type(JsonFieldType.STRING)
                            .description("ID"),
                    fieldWithPath("content.[].fromUser.id")
                            .type(JsonFieldType.STRING)
                            .description("사용자 ID"),
                    fieldWithPath("content.[].fromUser.name")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이름"),
                    fieldWithPath("content.[].fromUser.email")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이메일"),
                    fieldWithPath("content.[].fromUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("사용자 닉네임"),
                    fieldWithPath("content.[].fromUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("사용자 프로필 사진 주소"),
                    fieldWithPath("content.[].toUser.id")
                            .type(JsonFieldType.STRING)
                            .description("친구 ID"),
                    fieldWithPath("content.[].toUser.name")
                            .type(JsonFieldType.STRING)
                            .description("친구 이름"),
                    fieldWithPath("content.[].toUser.email")
                            .type(JsonFieldType.STRING)
                            .description("친구 이메일"),
                    fieldWithPath("content.[].toUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("친구 닉네임"),
                    fieldWithPath("content.[].toUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("친구 프로필 사진 주소"),
                    fieldWithPath("content.[].isHangingOut")
                            .type(JsonFieldType.BOOLEAN)
                            .description("놀러가기 여부")};

            mockMvc.perform(get("/api/v1/friends")
                            .queryParam("from-user-id", friend.getFromUser().getId().toString())
                            .queryParam("to-user-id", friend.getToUser().getId().toString())
                            .queryParam("is-hanging-out", Boolean.toString(friend.isHangingOut()))
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Origin", "*"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(response)))
                    .andDo(document("friend-get-list",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("친구 목록 전체를 조회합니다.")
                                    .summary("친구 조회")
                                    .requestParameters(
                                            parameterWithName("from-user-id")
                                                    .type(SimpleType.STRING)
                                                    .description("사용자 ID")
                                                    .optional(),
                                            parameterWithName("to-user-id")
                                                    .type(SimpleType.STRING)
                                                    .description("친구 ID")
                                                    .optional(),
                                            parameterWithName("is-hanging-out")
                                                    .type(SimpleType.BOOLEAN)
                                                    .description("놀러가기 여부")
                                                    .optional())
                                    .responseFields(
                                            withPageDescriptorsIgnored(responseFieldDescriptors))
                                    .build())));
        }
    }

    @Nested
    @DisplayName("친구 놀러가기")
    class HangOutWithFriendTest {

        @Test
        @DisplayName("성공")
        void getFriend_ok() throws Exception {
            UUID frId = UUID.randomUUID();

            Friend friend = Friend.builder()
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
                    .isHangingOut(false)
                    .build();

            FieldUtil.writeField(friend, "id", frId);

            Friend friendHangOut = Friend.builder()
                    .fromUser(friend.getFromUser())
                    .toUser(friend.getToUser())
                    .isHangingOut(true)
                    .build();

            FieldUtil.writeField(friendHangOut, "id", frId);

            FriendHangOutDto requestDto = new FriendHangOutDto(true);
            FriendResponseDto responseDto = friendMapper.toResponseDto(friendHangOut);

            given(friendService.hangOutWithFriend(frId, true))
                    .willReturn(friendHangOut);

            FieldDescriptor[] requestFieldDescriptors = {
                    fieldWithPath("isHangingOut")
                            .type(JsonFieldType.BOOLEAN)
                            .description("놀러가기 여부")};

            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("id")
                            .type(JsonFieldType.STRING)
                            .description("ID"),
                    fieldWithPath("fromUser.id")
                            .type(JsonFieldType.STRING)
                            .description("사용자 ID"),
                    fieldWithPath("fromUser.name")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이름"),
                    fieldWithPath("fromUser.email")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이메일"),
                    fieldWithPath("fromUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("사용자 닉네임"),
                    fieldWithPath("fromUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("사용자 프로필 사진 주소"),
                    fieldWithPath("toUser.id")
                            .type(JsonFieldType.STRING)
                            .description("친구 ID"),
                    fieldWithPath("toUser.name")
                            .type(JsonFieldType.STRING)
                            .description("친구 이름"),
                    fieldWithPath("toUser.email")
                            .type(JsonFieldType.STRING)
                            .description("친구 이메일"),
                    fieldWithPath("toUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("친구 닉네임"),
                    fieldWithPath("toUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("친구 프로필 사진 주소"),
                    fieldWithPath("isHangingOut")
                            .type(JsonFieldType.BOOLEAN)
                            .description("놀러가기 여부")};

            mockMvc.perform(put("/api/v1/friends/{id}", friend.getId())
                            .content(objectMapper.writeValueAsString(requestDto))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Origin", "*"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(responseDto)))
                    .andDo(document("friend-get-one",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("친구에게 놀러가기를 신청합니다.")
                                    .summary("친구 놀러가기")
                                    .requestFields(requestFieldDescriptors)
                                    .responseFields(responseFieldDescriptors)
                                    .build())));
        }
    }
}
