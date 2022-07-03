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
import java.util.Optional;
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
        // Save Request
        Review request_entity = reviewRepository.save(Review.builder()
                .user(user)
                .content(dto.getContent())
                .like(dto.getLike())
                .star(dto.getStar())
                .build());

        // Save foods
        List<String> foods = dto.getFoodList();
        List<ReviewFood> foodEntityList = foods.stream().map(food -> ReviewFood.builder()
                .review(request_entity)
                .food(food)
                .build()).collect(Collectors.toList());
        reviewFoodRepository.saveAll(foodEntityList);

        // Save Images
        List<String> images = dto.getImageList();
        List<ReviewImage> imageEntityList = images.stream().map(image -> ReviewImage.builder()
                .review(request_entity)
                .url(image)
                .build()).collect(Collectors.toList());
        reviewImageRepository.saveAll(imageEntityList);

        // Update Review (Modify Foreign Key)
        request_entity.setFoodList(foodEntityList);
        request_entity.setImageList(imageEntityList);

        return request_entity;
    }

    @Transactional
    public Review updateRequest(ReviewRequestUpdateServiceDto dto){
        // Checking Review Information
        String resourceName = Review.class.getSimpleName();
        UUID reviewId = dto.getReviewRequestId();
        User user;

        Optional<Review> optionalReview = Optional.ofNullable(findRequestById(dto.getReviewRequestId()));
        if(optionalReview.isPresent()){
            Review review = optionalReview.get();
            user = review.getUser();
            // Deleting existing review
            reviewRepository.delete(review);
        }else{
            throw new ResourceNotFoundException(resourceName, "Review ID" ,reviewId);
        }

        // Making New Review
        ReviewRequestCreateServiceDto createServiceDto = ReviewRequestCreateServiceDto.builder()
                .userId(user.getId())
                .content(dto.getContent())
                .like(dto.getLike())
                .star(dto.getStar())
                .foodList(dto.getFoodList())
                .imageList(dto.getImageList())
                .build();

        return create(createServiceDto);
    }

    @Transactional
    public void deleteRequestById(UUID id){
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException(Review.class.getSimpleName(), "id", id);
        }
        reviewRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Review findRequestById(UUID id){ // ReviewID로 검색
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        Review.class.getSimpleName(), "id", id));
    }

    @Transactional(readOnly = true)
    public Page<Review> findRequestsByUserId(UUID userId, Pageable pageable){
        return reviewRepository.findByUserId(userId, pageable);
    }
}

