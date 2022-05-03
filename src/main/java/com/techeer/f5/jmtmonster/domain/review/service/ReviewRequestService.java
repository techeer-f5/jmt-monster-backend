package com.techeer.f5.jmtmonster.domain.review.service;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateServiceDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewFoodRepository;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewImageRepository;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewRequestRepository;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewFood;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewImage;
import com.techeer.f5.jmtmonster.domain.review.domain.ReviewRequest;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestUpdateServiceDto;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.domain.user.repository.UserRepository;
import com.techeer.f5.jmtmonster.global.error.exception.DuplicateResourceException;
import com.techeer.f5.jmtmonster.global.error.exception.FieldErrorWrapper;
import com.techeer.f5.jmtmonster.global.error.exception.InnerResourceNotFoundException;
import com.techeer.f5.jmtmonster.global.error.exception.ResourceNotFoundException;
import com.techeer.f5.jmtmonster.s3.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewRequestService  {

    private final ReviewRequestRepository reviewRequestRepository;
    private final ReviewFoodRepository reviewFoodRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public ReviewRequest create(ReviewRequestCreateRequestDto dto){
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

        // Upload Images to S3 Bucket
        MultipartFile[] images = dto.getImageList();
        List<ReviewImage> imageEntityList = new ArrayList<>();
        for(MultipartFile image : images){
            try {
                String url = s3Uploader.upload(image, s3Uploader.getDIR_NAME())
                ReviewImage imageEntity = ReviewImage.builder()
                        .user(user)
                        .url(url)
                        .build();
                imageEntityList.add(imageEntity);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        reviewImageRepository.saveAll(imageEntityList);

        ReviewRequest request_entity = ReviewRequest.builder()
                .user(user)
                .content(dto.getContent())
                .like(dto.getLike())
                .star(dto.getStar())
                .build();

        return reviewRequestRepository.save(request_entity);
    }

    public ReviewRequest update(UUID id, ReviewRequestUpdateServiceDto dto){
        ReviewRequest entity = findOneById(id);
        User user = entity.getUser();

        // Save foods
        List<String> foods = dto.getFoodList();
        List<ReviewFood> foodEntityList = foods.stream().map(food -> ReviewFood.builder()
                .user(user)
                .food(food)
                .build()).collect(Collectors.toList());
        reviewFoodRepository.saveAll(foodEntityList);

        // Upload Images to S3 Bucket
        MultipartFile[] images = dto.getImageList();
        List<ReviewImage> imageEntityList = new ArrayList<>();
        for(MultipartFile image : images){
            try {
                String url = s3Uploader.upload(image, s3Uploader.getDIR_NAME())
                ReviewImage imageEntity = ReviewImage.builder()
                        .user(user)
                        .url(url)
                        .build();
                imageEntityList.add(imageEntity);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        reviewImageRepository.saveAll(imageEntityList);

        entity.update(
                user,
                dto.getContent(),
                dto.getLike(),
                dto.getStar()
        );
        return reviewRequestRepository.save(entity);
    }


    public void deleteById(UUID id){
        if (!reviewRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException(ReviewRequest.class.getSimpleName(), "id", id);
        }
        reviewRequestRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ReviewRequest findOneById(UUID id){
        return reviewRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ReviewRequest.class.getSimpleName(), "id", id));
    }

    @Transactional(readOnly = true)
    public Page<ReviewRequest> findAll(Pageable pageable){
        return reviewRequestRepository.findAll(pageable);
    }

}

