package com.techeer.f5.jmtmonster.domain.friend.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.friend.domain.Friend;
import com.techeer.f5.jmtmonster.domain.friend.dto.mapper.FriendMapper;
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
@DisplayName("?????? API")
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
    @DisplayName("?????? ?????? ??????")
    class GetFriendTest {

        @Test
        @DisplayName("??????")
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
                            .description("????????? ID"),
                    fieldWithPath("fromUser.name")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("fromUser.email")
                            .type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                    fieldWithPath("fromUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                    fieldWithPath("fromUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("????????? ????????? ?????? ??????"),
                    fieldWithPath("toUser.id")
                            .type(JsonFieldType.STRING)
                            .description("?????? ID"),
                    fieldWithPath("toUser.name")
                            .type(JsonFieldType.STRING)
                            .description("?????? ??????"),
                    fieldWithPath("toUser.email")
                            .type(JsonFieldType.STRING)
                            .description("?????? ?????????"),
                    fieldWithPath("toUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("?????? ?????????"),
                    fieldWithPath("toUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("?????? ????????? ?????? ??????")};

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
                                    .description("?????? ??? ?????? ???????????????.")
                                    .summary("?????? ??????")
                                    .responseFields(responseFieldDescriptors)
                                    .build())));
        }
    }

    @Nested
    @DisplayName("?????? ?????? ??????")
    class GetFriendListTest {

        @Test
        @DisplayName("?????? ?????? ?????? - ??????")
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
                    .build();

            FieldUtil.writeField(friend, "id", UUID.randomUUID());

            List<Friend> content = List.of(friend);

            Page<Friend> pageResponse = new PageImpl<>(content, PageRequest.of(0, 10),
                    content.size());

            Page<FriendResponseDto> response = pageResponse.map(friendMapper::toResponseDto);

            given(friendService.findAllFriends(
                    any(),
                    eq(friend.getFromUser().getId()),
                    eq(friend.getToUser().getId())
            )).willReturn(pageResponse);

            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("content.[].id")
                            .type(JsonFieldType.STRING)
                            .description("ID"),
                    fieldWithPath("content.[].fromUser.id")
                            .type(JsonFieldType.STRING)
                            .description("????????? ID"),
                    fieldWithPath("content.[].fromUser.name")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("content.[].fromUser.email")
                            .type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                    fieldWithPath("content.[].fromUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                    fieldWithPath("content.[].fromUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("????????? ????????? ?????? ??????"),
                    fieldWithPath("content.[].toUser.id")
                            .type(JsonFieldType.STRING)
                            .description("?????? ID"),
                    fieldWithPath("content.[].toUser.name")
                            .type(JsonFieldType.STRING)
                            .description("?????? ??????"),
                    fieldWithPath("content.[].toUser.email")
                            .type(JsonFieldType.STRING)
                            .description("?????? ?????????"),
                    fieldWithPath("content.[].toUser.nickname")
                            .type(JsonFieldType.STRING)
                            .description("?????? ?????????"),
                    fieldWithPath("content.[].toUser.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("?????? ????????? ?????? ??????")};

            mockMvc.perform(get("/api/v1/friends")
                            .queryParam("from-user-id", friend.getFromUser().getId().toString())
                            .queryParam("to-user-id", friend.getToUser().getId().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Origin", "*"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(response)))
                    .andDo(document("friend-get-list",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("?????? ?????? ????????? ???????????????.")
                                    .summary("?????? ??????")
                                    .requestParameters(
                                            parameterWithName("from-user-id")
                                                    .type(SimpleType.STRING)
                                                    .description("????????? ID")
                                                    .optional(),
                                            parameterWithName("to-user-id")
                                                    .type(SimpleType.STRING)
                                                    .description("?????? ID")
                                                    .optional(),
                                            parameterWithName("is-hanging-out")
                                                    .type(SimpleType.BOOLEAN)
                                                    .description("???????????? ??????")
                                                    .optional())
                                    .responseFields(
                                            withPageDescriptorsIgnored(responseFieldDescriptors))
                                    .build())));
        }
    }
}
