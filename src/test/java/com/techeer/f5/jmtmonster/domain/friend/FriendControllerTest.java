package com.techeer.f5.jmtmonster.domain.friend;


import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.friend.dao.FriendRequestRepository;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequestStatus;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendResponseDto;
import com.techeer.f5.jmtmonster.domain.oauth.domain.AuthProvider;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.UUID;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
// For use test db
@ActiveProfiles(profiles = {"test"})
@Slf4j
public class FriendControllerTest {
    // See https://spring.io/guides/gs/testing-restdocs/
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private JsonMapper jsonMapper;

    private User userOne;
    private User userTwo;

    private PersistentToken userOneToken;
    private PersistentToken userTwoToken;

    @BeforeEach
    @Transactional
    public void setEntities() {
        userOne = User.builder().name("이지호").email("optional.int@kakao.com").build();
        userTwo = User.builder().name("최우석").email("wschoe@kakao.com").build();

        userOne = userRepository.saveAndFlush(userOne);
        userTwo = userRepository.saveAndFlush(userTwo);

        userOneToken = PersistentToken.builder().user(userOne).provider(AuthProvider.KAKAO).build();
        userTwoToken = PersistentToken.builder().user(userTwo).provider(AuthProvider.KAKAO).build();

        persistentTokenRepository.saveAndFlush(userOneToken);
        persistentTokenRepository.saveAndFlush(userTwoToken);

        userOne.addToken(userOneToken);
        userTwo.addToken(userTwoToken);

    }


    @Test
    @Transactional(readOnly = true)
    public void testFriendApi() throws Exception {

        // Post friend requests
        FriendRequestCreateRequestDto requestDto = FriendRequestCreateRequestDto.builder()
                .fromUserId(userOne.getId())
                .toUserId(userTwo.getId())
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/friend-requests")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userOneToken.getId())))
                .andDo(print())
                .andExpect(status().is(201))
                .andReturn();

        // Extract post result
        Map<String, Object> result = jsonMapper.fromMvcResult(mvcResult, new TypeReference<Map<String, Object>>() {});

        UUID id = UUID.fromString((String) result.get("id"));

        log.info(String.format("friend request id: %s", id.toString()));

        // Set accepted status with put (it's necessary)
        FriendRequestUpdateRequestDto friendRequestUpdateRequestDto = FriendRequestUpdateRequestDto.builder()
                                                                        .status(FriendRequestStatus.ACCEPTED)
                                                                        .build();

        requestBody = objectMapper.writeValueAsString(friendRequestUpdateRequestDto);


        // Friend entity is saved at this time.
        mockMvc.perform(RestDocumentationRequestBuilders.put(String.format("/api/v1/friend-requests/%s", id))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userOneToken.getId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        FriendRequest friendRequest = friendRequestRepository.getById(id);

        log.info(friendRequest.toString());

        mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/friends")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userOneToken.getId())))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        // type check
        var friendResponseDtos = jsonMapper.fromMvcResult(mvcResult, new TypeReference<Page<FriendResponseDto>>() {}).get().toList();

        log.info("friendRequestResponseDto: " + friendResponseDtos.stream()
                                                                    .map(Object::toString)
                                                                    .reduce("", (a, b) -> a + b + " | "));

        // check every boolean field of every element has "is" prefix
        boolean hasIsPrefix = friendResponseDtos.stream()
                .map(e -> objectMapper.convertValue(e, new TypeReference<Map<String, Object>>() {}))
                .map(e -> e.entrySet()
                            .stream()
                            .filter((entry) -> entry.getValue() instanceof Boolean)
                            .filter((entry) -> !entry.getKey().startsWith("is"))
                            .toList()
                            .isEmpty())
                .reduce(true, (a, b) -> a && b);


        assertTrue(hasIsPrefix);
    }

}
