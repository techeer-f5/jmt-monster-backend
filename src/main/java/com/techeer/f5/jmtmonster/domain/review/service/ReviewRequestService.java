package com.techeer.f5.jmtmonster.domain.review.service;

import com.techeer.f5.jmtmonster.domain.review.dao.ReviewFoodRepository;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewImageRepository;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewRepository;
import com.techeer.f5.jmtmonster.domain.review.domain.Review;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewFood;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewImage;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.error.exception.DuplicateResourceException;
import com.techeer.f5.jmtmonster.global.error.exception.FieldErrorWrapper;
import com.techeer.f5.jmtmonster.global.error.exception.InnerResourceNotFoundException;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewRequestService  {

    private final ReviewRepository reviewRepository;
    private final ReviewFoodRepository reviewFoodRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserRepository userRepository;

    public Review create(ReviewRequestCreateServiceDto dto){
        // Check user information
        UUID userId = dto.getUserId();
        String resourceName = Review.class.getSimpleName();

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new InnerResourceNotFoundException(
                        resourceName,
                        List.of(new FieldErrorWrapper(
                                User.class.getSimpleName(), "userId",
                                userId.toString(), "not found"))));

        // Check existing condition
        if (reviewRepository.existsById(userId)) {
            throw new DuplicateResourceException(resourceName,
                    List.of(new FieldErrorWrapper(resourceName, "userId", userId.toString(),
                                    "already exists with userId")));
        }
        // Save foods
        List<String> foods = dto.getFoodList();
        List<ReviewFood> foodEntityList = foods.stream().map(food -> ReviewFood.builder()
                .review(null)
                .food(food)
                .build()).collect(Collectors.toList());
        reviewFoodRepository.saveAll(foodEntityList);

        // Save Images
        List<String> images = dto.getImageList();
        List<ReviewImage> imageEntityList = images.stream().map(image -> ReviewImage.builder()
                .review(null)
                .url(image)
                .build()).collect(Collectors.toList());
        reviewImageRepository.saveAll(imageEntityList);

        // Save Request
        Review request_entity = Review.builder()
                .user(user)
                .content(dto.getContent())
                .like(dto.getLike())
                .star(dto.getStar())
                .build();

        // Update Foods (Modify Foreign Key)
        for(ReviewFood food : foodEntityList){
            food.setReview(request_entity);
            reviewFoodRepository.save(food);
        }

        // Update Images (Modify Foreign Key)
        for(ReviewImage image : imageEntityList){
            image.setReview(request_entity);
            reviewImageRepository.save(image);
        }
         // JPA는 변경감지를 하여 SAVE 메소드로 UPDATE 쿼리문 생성 가능

        return reviewRepository.save(request_entity);
        // 최종적으로 REVIEW 엔티티 저장
    }

    public Review updateRequest(UUID reviewId, ReviewRequestUpdateServiceDto dto){
        Review entity = findRequestById(reviewId);
        User user = entity.getUser();

        // Save foods
        List<String> foods = dto.getFoodList();
        List<ReviewFood> foodEntityList = foods.stream().map(food -> ReviewFood.builder()
                .review(entity)
                .food(food)
                .build()).collect(Collectors.toList());
        reviewFoodRepository.saveAll(foodEntityList);

        // Save images
        List<String> images = dto.getImageList();
        List<ReviewImage> imageEntityList = images.stream().map(image -> ReviewImage.builder()
                .review(entity)
                .url(image)
                .build()).collect(Collectors.toList());
        reviewImageRepository.saveAll(imageEntityList);

        entity.update(
                user,
                dto.getContent(),
                dto.getLike(),
                dto.getStar()
        );
        return reviewRepository.save(entity);
    }


    public void deleteRequestById(UUID id){
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException(Review.class.getSimpleName(), "id", id);
        }
        reviewRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Review findRequestById(UUID id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Review.class.getSimpleName(), "id", id));
    }

    @Transactional(readOnly = true)
    public Page<Review> findAllRequests(Pageable pageable){
        return reviewRepository.findAll(pageable);
    }



}

