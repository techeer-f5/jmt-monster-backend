package com.techeer.f5.jmtmonster.domain.review.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.review.domain.*;
import com.techeer.f5.jmtmonster.domain.review.dto.mapper.ReviewMapper;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestUpdateRequestDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import static com.techeer.f5.jmtmonster.document.util.ResponseFieldDescriptorUtils.withPageDescriptorsIgnored;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ActiveProfiles(profiles = {"secret", "test", "disable-auth"})
@Import({ReviewMapper.class, UserMapper.class})
@DisplayName("리뷰 API")
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewMapper mapper;

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

    @Nested
    @DisplayName("REVIEW 단일 생성")
    class CreateTest{

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

    @Nested
    @DisplayName("REVIEW 단일 조회")
    class GetOneTest{

        @Test
        @DisplayName("성공")
        void GetOneSuccessTest() throws Exception {
            // given
            System.out.printf("REVIEW : %s",givenReview);
            ReviewRequestResponseDto responseDto = mapper.toResponseDto(givenReview);

            given(service.findRequestById(any())).willReturn(givenReview);

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

            mockMvc.perform(get("/api/v1/reviews/{id}",givenReview.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(responseDto)))
                    .andDo(print())
                    .andDo(document("review-get-one",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("리뷰를 하나 조회합니다.")
                                    .summary("리뷰 단일 조회")
                                    .pathParameters(
                                            parameterWithName("id")
                                                    .type(SimpleType.STRING)
                                                    .description("REVIEW ID"))
                                    .responseFields(responseFieldDescriptors)
                                    .build())));
        }
    }

    @Nested
    @DisplayName("REVIEW 페이지 조회")
    class GetListTest {

        @Test
        @DisplayName("성공")
        void GetListSuccessTest() throws Exception {
            // given
            List<Review> content = List.of(givenReview);

            Page<Review> pageResponse = new PageImpl<>(content, PageRequest.of(0, 10),
                    content.size());

            Page<ReviewRequestResponseDto> response = pageResponse.map(mapper::toResponseDto);

            given(service.findRequestsByUserId(any(),any())).willReturn(pageResponse);

            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("content.[].id")
                            .type(JsonFieldType.STRING)
                            .description("REVIEW ID"),
                    fieldWithPath("content.[].user.id")
                            .type(JsonFieldType.STRING)
                            .description("사용자 ID"),
                    fieldWithPath("content.[].user.name")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이름"),
                    fieldWithPath("content.[].user.email")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이메일"),
                    fieldWithPath("content.[].user.nickname")
                            .type(JsonFieldType.STRING)
                            .description("사용자 별명"),
                    fieldWithPath("content.[].user.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("사용자 이미지 url")
                            .optional()};

            // when

            // then
            mockMvc.perform(get("/api/v1/reviews")
                            .queryParam("user-id",givenUser.getId().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(response)))
                    .andDo(print())
                    .andDo(document("review-get-list",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("리뷰 목록을 사용자 ID를 이용하여 조회합니다.")
                                    .summary("리뷰 목록 조회")
                                    .requestParameters(
                                            parameterWithName("user-id")
                                                    .type(SimpleType.STRING)
                                                    .description("사용자 ID"))
                                    .responseFields(
                                            withPageDescriptorsIgnored(responseFieldDescriptors))
                                    .build())));
        }
    }

    @Nested
    @DisplayName("REVIEW 업데이트")
    class UpdateTest{

        @Test
        @DisplayName("성공")
        void UpdateSuccessTest() throws Exception {
            // given
            ReviewRequestUpdateRequestDto requestDto = ReviewRequestUpdateRequestDto.builder()
                    .reviewRequestId(givenReview.getId())
                    .content("Changed content")
                    .like(Like.DISLIKE)
                    .star(Star.FOUR)
                    .foodList(List.of("new food 1","new food 2"))
                    .imageList(List.of("new url 1","new url 2"))
                    .build();

            Review newReview = Review.builder()
                    .id(givenReview.getId())
                    .user(givenUser)
                    .content(requestDto.getContent())
                    .like(requestDto.getLike())
                    .star(requestDto.getStar())
                    .foodList(null)
                    .imageList(null)
                    .build();

            ReviewFood food1 = ReviewFood.builder()
                    .review(givenReview)
                    .food("new food 1")
                    .build();
            ReviewFood food2 = ReviewFood.builder()
                    .review(givenReview)
                    .food("new food 2")
                    .build();
            List<ReviewFood> foods = List.of(food1, food2);

            ReviewImage image1 = ReviewImage.builder()
                    .review(givenReview)
                    .url("new url 1")
                    .build();
            ReviewImage image2 = ReviewImage.builder()
                    .review(givenReview)
                    .url("new url 2")
                    .build();
            List<ReviewImage> images = List.of(image1, image2);

            newReview.setFoodList(foods);
            newReview.setImageList(images);

            ReviewRequestResponseDto response = mapper.toResponseDto(newReview);

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

            given(service.updateRequest(any())).willReturn(newReview);

            // when

            // then
            mockMvc.perform(put("/api/v1/reviews")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(response)))
                    .andDo(print())
                    .andDo(document("review-update",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("리뷰를 지우고 새로 만듦으로써 업데이트 합니다..")
                                    .summary("리뷰 업데이트")
                                    .responseFields(
                                            responseFieldDescriptors)
                                    .build())));
        }

    }

    @Nested
    @DisplayName("REVIEW 삭제")
    class DeleteTest {

        @Test
        @DisplayName("성공")
        void DeleteSuccessTest() throws Exception {
            // given
            doNothing().when(service).deleteRequestById(givenReview.getId());

            // when

            // then
            mockMvc.perform(delete("/api/v1/reviews/{id}",givenReview.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andDo(document("review-get-list",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("리뷰 아이디를 이용하여 리뷰를 삭제합니다.")
                                    .summary("리뷰 단일 삭제")
                                    .pathParameters(
                                            parameterWithName("id")
                                                    .type(SimpleType.STRING)
                                                    .description("REVIEW ID"))
                                    .build())));
        }

    }



}
