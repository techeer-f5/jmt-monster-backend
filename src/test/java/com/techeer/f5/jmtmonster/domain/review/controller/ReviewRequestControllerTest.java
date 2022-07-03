package com.techeer.f5.jmtmonster.domain.review.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.friend.controller.FriendController;
import com.techeer.f5.jmtmonster.domain.friend.dto.mapper.FriendMapper;
import com.techeer.f5.jmtmonster.domain.friend.service.FriendService;
import com.techeer.f5.jmtmonster.domain.review.domain.*;
import com.techeer.f5.jmtmonster.domain.review.dto.mapper.ReviewRequestMapper;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ReviewRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.review.service.ReviewRequestService;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.dto.UserMapper;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.techeer.f5.jmtmonster.document.util.ResponseFieldDescriptorUtils.withHateOasDescriptorsIgnored;
import static com.techeer.f5.jmtmonster.document.util.ResponseFieldDescriptorUtils.withPageDescriptorsIgnored;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewRequestController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ActiveProfiles(profiles = {"secret", "test", "disable-auth"})
@Import({ReviewRequestMapper.class, UserMapper.class})
@DisplayName("리뷰 API")
public class ReviewRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewRequestMapper mapper;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private ReviewRequestService service;

    private User givenUser;
    private ReviewFood givenFood1;
    private ReviewFood givenFood2;
    private List<ReviewFood> givenFoods;
    private ReviewImage givenImage1;
    private ReviewImage givenImage2;
    private List<ReviewImage> givenImages;
    private Review givenReview;

    @Nested
    @DisplayName("REVIEW 단일 생성")
    class CreateTest{

        @BeforeEach
        void setUp() {
            givenUser = User.builder()
                    .id(UUID.randomUUID())
                    .email("tester1@example.com")
                    .name("Tester1")
                    .nickname("Tester1")
                    .build();

            givenReview = Review.builder()
                    .id(UUID.randomUUID())
                    .user(givenUser)
                    .content("Test content")
                    .like(Like.LIKE)
                    .star(Star.FIVE)
                    .foodList(null)
                    .imageList(null)
                    .build();

            givenFood1 = ReviewFood.builder()
                    .id(UUID.randomUUID())
                    .review(givenReview)
                    .food("Test food 1")
                    .build();
            givenFood2 = ReviewFood.builder()
                    .id(UUID.randomUUID())
                    .review(givenReview)
                    .food("Test food 2")
                    .build();
            givenFoods = List.of(givenFood1, givenFood2);

            givenImage1 = ReviewImage.builder()
                    .id(UUID.randomUUID())
                    .review(givenReview)
                    .url("Test URL 1")
                    .build();
            givenImage2 = ReviewImage.builder()
                    .id(UUID.randomUUID())
                    .review(givenReview)
                    .url("Test URL 2")
                    .build();
            givenImages = List.of(givenImage1, givenImage2);

            givenReview.setFoodList(givenFoods);
            givenReview.setImageList(givenImages);
        }

        @Test
        @DisplayName("성공")
        void CreateSuccessTest() throws Exception {
            // given
            ReviewRequestCreateRequestDto requestDto = ReviewRequestCreateRequestDto.builder()
                    .userId(givenUser.getId())
                    .content("Test content")
                    .like(Like.LIKE)
                    .star(Star.ONE)
                    .foodList(List.of("Test food 1","Test food 2"))
                    .imageList(List.of("Test URL 1","Test URL 2"))
                    .build();

            ReviewRequestResponseDto responseDto = mapper.toResponseDto(givenReview);

            given(service.create(any())).willReturn(givenReview);

            // when

            // then
            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("id")
                            .type(JsonFieldType.STRING)
                            .description("REVIEW ID"),
                    fieldWithPath("user.id")
                            .type(JsonFieldType.STRING)
                            .description("사용자 ID"),
                    fieldWithPath("user.name")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이름"),
                    fieldWithPath("user.email")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이메일"),
                    fieldWithPath("user.nickname")
                            .type(JsonFieldType.STRING)
                            .description("사용자 별명"),
                    fieldWithPath("user.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이미지 url")
                            .optional()};

            mockMvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(responseDto)))
                    .andDo(print())
                    .andDo(document("review-create",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("리뷰를 하나 생성합니다.")
                                    .summary("리뷰 생성")
                                    .requestFields(
                                            fieldWithPath("userId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("사용자 ID"),
                                            fieldWithPath("content")
                                                    .type(JsonFieldType.STRING)
                                                    .description("리뷰 내용"),
                                            fieldWithPath("like")
                                                    .type(JsonFieldType.STRING)
                                                    .description("좋아요 여부 (LIKE)"),
                                            fieldWithPath("star")
                                                    .type(JsonFieldType.STRING)
                                                    .description("별점 (FIVE)"),
                                            fieldWithPath("foodList")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("먹은 음식들"),
                                            fieldWithPath("imageList")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("이미지 url들"))
                                    .responseFields(responseFieldDescriptors)
                                    .build())));
        }
    }


}
