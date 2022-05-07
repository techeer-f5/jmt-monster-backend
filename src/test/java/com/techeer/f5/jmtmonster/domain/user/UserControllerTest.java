package com.techeer.f5.jmtmonster.domain.user;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
// For use test db
@TestPropertySource("classpath:application-test.yml")
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

        user = userRepository.saveAndFlush(user);

        PersistentToken persistentToken = PersistentToken.builder().user(user).provider(AuthProvider.KAKAO).build();

        persistentToken = persistentTokenRepository.saveAndFlush(persistentToken);
        user.getTokens().add(persistentToken);

        MvcResult mvcResult = mockMvc.perform(get("/users/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + persistentToken.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
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

        MvcResult mvcResult = mockMvc.perform(post("/users/me/extra-info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + persistentToken.getId().toString())
                        .content(JsonMapper.asJsonString(extraUserInfoRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
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
