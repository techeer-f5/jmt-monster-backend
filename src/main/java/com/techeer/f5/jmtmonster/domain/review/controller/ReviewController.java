package com.techeer.f5.jmtmonster.domain.review.controller;

import com.techeer.f5.jmtmonster.domain.review.domain.Review;
import com.techeer.f5.jmtmonster.domain.review.dto.mapper.ReviewMapper;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ReviewResponseDto;
import com.techeer.f5.jmtmonster.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;
    private final ReviewMapper mapper;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(
            @Valid @RequestBody ReviewCreateRequestDto dto
            ){
        Review entity = service.create(mapper.toServiceDto(dto));
        ReviewResponseDto response = mapper.toResponseDto(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getOne(@PathVariable UUID id) {
        return ResponseEntity
                .ok(mapper.toResponseDto(service.findRequestById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> getList( // User ID로 Pagination
                                                            @RequestParam(value = "user-id") UUID userId,
                                                            @PageableDefault(size = 20, sort = "createdOn", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        return ResponseEntity
                .ok(service.findRequestsByUserId(userId, pageable).map(mapper::toResponseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewUpdateRequestDto dto
    ) {
        return ResponseEntity
                .ok(mapper.toResponseDto(service.updateRequest(mapper.toServiceDto(dto, id))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteRequestById(id); // Review ID로 삭제
        return ResponseEntity
                .noContent()
                .build();
    }
}
