package com.techeer.f5.jmtmonster.domain.review.service;

import com.techeer.f5.jmtmonster.domain.review.dao.ReviewFoodRepository;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewImageRepository;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewRequestRepository;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewFood;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewRequest;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.error.exception.DuplicateResourceException;
import com.techeer.f5.jmtmonster.global.error.exception.FieldErrorWrapper;
import com.techeer.f5.jmtmonster.global.error.exception.InnerResourceNotFoundException;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
import com.techeer.f5.jmtmonster.s3.util.S3ManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewRequestService  {

    private final ReviewRequestRepository reviewRequestRepository;
    private final ReviewFoodRepository reviewFoodRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserRepository userRepository;
    private final S3ManagerImpl s3Manager;

    public String uploadImage(MultipartFile image) throws IOException {
        // Upload Images to S3 Bucket
        String url = s3Manager.upload(image, Optional.empty());
        return url;
    }

    public String deleteImage(String url){
        return "pass";
    }

    public String updateImage(String url){
        return "pass";
    }

    public ReviewRequest create(ReviewRequestCreateServiceDto dto){
        // Check user information
        UUID userId = dto.getUserId();
        String resourceName = ReviewRequest.class.getSimpleName();

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new InnerResourceNotFoundException(
                        resourceName,
                        List.of(new FieldErrorWrapper(
                                User.class.getSimpleName(), "userId",
                                userId.toString(), "not found"))));

        // Check existing condition
        if (reviewRequestRepository.existsById(userId)) {
            throw new DuplicateResourceException(resourceName,
                    List.of(new FieldErrorWrapper(resourceName, "userId", userId.toString(),
                                    "already exists with userId")));
        }
        // Save foods
        List<String> foods = dto.getFoodList();
        List<ReviewFood> foodEntityList = foods.stream().map(food -> ReviewFood.builder()
                .user(user)
                .food(food)
                .build()).collect(Collectors.toList());
        reviewFoodRepository.saveAll(foodEntityList);

        ReviewRequest request_entity = ReviewRequest.builder()
                .user(user)
                .content(dto.getContent())
                .likeStatus(dto.getLike())
                .star(dto.getStar())
                .build();

        return reviewRequestRepository.save(request_entity);
    }

    public ReviewRequest updateRequest(UUID id, ReviewRequestUpdateServiceDto dto){
        ReviewRequest entity = findRequestById(id);
        User user = entity.getUser();

        // Save foods
        List<String> foods = dto.getFoodList();
        List<ReviewFood> foodEntityList = foods.stream().map(food -> ReviewFood.builder()
                .user(user)
                .food(food)
                .build()).collect(Collectors.toList());
        reviewFoodRepository.saveAll(foodEntityList);

        entity.update(
                dto.getContent(),
                dto.getLike(),
                dto.getStar()
        );
        return reviewRequestRepository.save(entity);
    }


    public void deleteRequestById(UUID id){
        if (!reviewRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException(ReviewRequest.class.getSimpleName(), "id", id);
        }
        reviewRequestRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ReviewRequest findRequestById(UUID id){
        return reviewRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ReviewRequest.class.getSimpleName(), "id", id));
    }

    @Transactional(readOnly = true)
    public Page<ReviewRequest> findAllRequests(Pageable pageable){
        return reviewRequestRepository.findAll(pageable);
    }



}

