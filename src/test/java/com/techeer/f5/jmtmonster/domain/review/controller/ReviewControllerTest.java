package com.techeer.f5.jmtmonster.domain.review.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.SimpleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.f5.jmtmonster.domain.review.domain.*;
import com.techeer.f5.jmtmonster.domain.review.dto.mapper.ReviewMapper;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ReviewResponseDto;
import com.techeer.f5.jmtmonster.domain.review.service.ReviewService;
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
@ActiveProfiles(profiles = {"test", "disable-auth"})
@Import({ReviewMapper.class, UserMapper.class})
@DisplayName("?????? API")
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewMapper mapper;

    @MockBean
    private ReviewService service;

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
    @DisplayName("REVIEW ?????? ??????")
    class CreateTest{

        @Test
        @DisplayName("??????")
        void CreateSuccessTest() throws Exception {
            // given
            ReviewCreateRequestDto requestDto = ReviewCreateRequestDto.builder()
                    .userId(givenUser.getId())
                    .content("Test content")
                    .like(Like.LIKE)
                    .star(Star.ONE)
                    .foodList(List.of("Test food 1","Test food 2"))
                    .imageList(List.of("Test URL 1","Test URL 2"))
                    .build();

            ReviewResponseDto responseDto = mapper.toResponseDto(givenReview);

            given(service.create(any())).willReturn(givenReview);

            // when & then
            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("id")
                            .type(JsonFieldType.STRING)
                            .description("REVIEW ID"),
                    fieldWithPath("user.id")
                            .type(JsonFieldType.STRING)
                            .description("????????? ID"),
                    fieldWithPath("user.name")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("user.email")
                            .type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                    fieldWithPath("user.nickname")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("user.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("????????? ????????? url")
                            .optional()};

            mockMvc.perform(post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding("utf-8"))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(objectMapper.writeValueAsString(responseDto)))
                    .andDo(print())
                    .andDo(document("review-create",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("????????? ?????? ???????????????.")
                                    .summary("?????? ??????")
                                    .requestFields(
                                            fieldWithPath("userId")
                                                    .type(JsonFieldType.STRING)
                                                    .description("????????? ID"),
                                            fieldWithPath("content")
                                                    .type(JsonFieldType.STRING)
                                                    .description("?????? ??????"),
                                            fieldWithPath("like")
                                                    .type(JsonFieldType.STRING)
                                                    .description("????????? ?????? (LIKE)"),
                                            fieldWithPath("star")
                                                    .type(JsonFieldType.STRING)
                                                    .description("?????? (FIVE)"),
                                            fieldWithPath("foodList")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("?????? ?????????"),
                                            fieldWithPath("imageList")
                                                    .type(JsonFieldType.ARRAY)
                                                    .description("????????? url???"))
                                    .responseFields(responseFieldDescriptors)
                                    .build())));
        }
    }

    @Nested
    @DisplayName("REVIEW ?????? ??????")
    class GetOneTest{

        @Test
        @DisplayName("??????")
        void GetOneSuccessTest() throws Exception {
            // given
            System.out.printf("REVIEW : %s",givenReview);
            ReviewResponseDto responseDto = mapper.toResponseDto(givenReview);

            given(service.findRequestById(any())).willReturn(givenReview);

            // when & then
            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("id")
                            .type(JsonFieldType.STRING)
                            .description("REVIEW ID"),
                    fieldWithPath("user.id")
                            .type(JsonFieldType.STRING)
                            .description("????????? ID"),
                    fieldWithPath("user.name")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("user.email")
                            .type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                    fieldWithPath("user.nickname")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("user.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("????????? ????????? url")
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
                                    .description("????????? ?????? ???????????????.")
                                    .summary("?????? ?????? ??????")
                                    .pathParameters(
                                            parameterWithName("id")
                                                    .type(SimpleType.STRING)
                                                    .description("REVIEW ID"))
                                    .responseFields(responseFieldDescriptors)
                                    .build())));
        }
    }

    @Nested
    @DisplayName("REVIEW ????????? ??????")
    class GetListTest {

        @Test
        @DisplayName("??????")
        void GetListSuccessTest() throws Exception {
            // given
            List<Review> content = List.of(givenReview);

            Page<Review> pageResponse = new PageImpl<>(content, PageRequest.of(0, 10),
                    content.size());

            Page<ReviewResponseDto> response = pageResponse.map(mapper::toResponseDto);

            given(service.findRequestsByUserId(any(),any())).willReturn(pageResponse);

            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("content.[].id")
                            .type(JsonFieldType.STRING)
                            .description("REVIEW ID"),
                    fieldWithPath("content.[].user.id")
                            .type(JsonFieldType.STRING)
                            .description("????????? ID"),
                    fieldWithPath("content.[].user.name")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("content.[].user.email")
                            .type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                    fieldWithPath("content.[].user.nickname")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("content.[].user.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("????????? ????????? url")
                            .optional()};

            // when & then
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
                                    .description("?????? ????????? ????????? ID??? ???????????? ???????????????.")
                                    .summary("?????? ?????? ??????")
                                    .requestParameters(
                                            parameterWithName("user-id")
                                                    .type(SimpleType.STRING)
                                                    .description("????????? ID"))
                                    .responseFields(
                                            withPageDescriptorsIgnored(responseFieldDescriptors))
                                    .build())));
        }
    }

    @Nested
    @DisplayName("REVIEW ????????????")
    class UpdateTest{

        @Test
        @DisplayName("??????")
        void UpdateSuccessTest() throws Exception {
            // given
            ReviewUpdateRequestDto requestDto = ReviewUpdateRequestDto.builder()
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

            ReviewResponseDto response = mapper.toResponseDto(newReview);

            FieldDescriptor[] responseFieldDescriptors = {
                    fieldWithPath("id")
                            .type(JsonFieldType.STRING)
                            .description("REVIEW ID"),
                    fieldWithPath("user.id")
                            .type(JsonFieldType.STRING)
                            .description("????????? ID"),
                    fieldWithPath("user.name")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("user.email")
                            .type(JsonFieldType.STRING)
                            .description("????????? ?????????"),
                    fieldWithPath("user.nickname")
                            .type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                    fieldWithPath("user.imageUrl")
                            .type(JsonFieldType.STRING)
                            .description("????????? ????????? url")
                            .optional()};

            given(service.updateRequest(any())).willReturn(newReview);

            // when & then
            mockMvc.perform(put("/api/v1/reviews/{id}",givenReview.getId())
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
                                    .description("????????? ????????? ?????? ??????????????? ???????????? ?????????..")
                                    .summary("?????? ????????????")
                                    .responseFields(
                                            responseFieldDescriptors)
                                    .build())));
        }

    }

    @Nested
    @DisplayName("REVIEW ??????")
    class DeleteTest {

        @Test
        @DisplayName("??????")
        void DeleteSuccessTest() throws Exception {
            // given
            doNothing().when(service).deleteRequestById(givenReview.getId());

            // when & then
            mockMvc.perform(delete("/api/v1/reviews/{id}",givenReview.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andDo(document("review-get-list",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            resource(ResourceSnippetParameters.builder()
                                    .description("?????? ???????????? ???????????? ????????? ???????????????.")
                                    .summary("?????? ?????? ??????")
                                    .pathParameters(
                                            parameterWithName("id")
                                                    .type(SimpleType.STRING)
                                                    .description("REVIEW ID"))
                                    .build())));
        }

    }



}
