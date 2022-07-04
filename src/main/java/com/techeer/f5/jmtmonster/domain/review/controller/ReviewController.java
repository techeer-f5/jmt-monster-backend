package com.techeer.f5.jmtmonster.domain.review.controller;

import com.techeer.f5.jmtmonster.domain.review.domain.Review;
import com.techeer.f5.jmtmonster.domain.review.dto.mapper.ReviewMapper;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ReviewRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.review.service.ReviewRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRequestService service;
    private final ReviewMapper mapper;

    @PostMapping
    public ResponseEntity<ReviewRequestResponseDto> create(
            @Valid @RequestBody ReviewRequestCreateRequestDto dto
            ){
        Review entity = service.create(mapper.toServiceDto(dto));
        ReviewRequestResponseDto response = mapper.toResponseDto(entity);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewRequestResponseDto> getOne(@PathVariable UUID id) {
        return ResponseEntity
                .ok(mapper.toResponseDto(service.findRequestById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<ReviewRequestResponseDto>> getList( // User ID로 Pagination
            @RequestParam(value = "user-id") UUID userId,
            @PageableDefault(size = 20, sort = "createdOn", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        return ResponseEntity
                .ok(service.findRequestsByUserId(userId, pageable).map(mapper::toResponseDto));
    }

    @PutMapping
    public ResponseEntity<ReviewRequestResponseDto> update(
            @Valid @RequestBody ReviewRequestUpdateRequestDto dto
    ) {
        return ResponseEntity
                .ok(mapper.toResponseDto(service.updateRequest(mapper.toServiceDto(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteRequestById(id); // Review ID로 삭제
        return ResponseEntity
                .noContent()
                .build();
    }
}
