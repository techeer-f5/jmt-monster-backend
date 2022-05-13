package com.techeer.f5.jmtmonster.domain.review.controller;

import com.techeer.f5.jmtmonster.domain.review.domain.ReviewRequest;
import com.techeer.f5.jmtmonster.domain.review.dto.mapper.ReviewRequestMapper;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestModel;
import com.techeer.f5.jmtmonster.domain.review.dto.request.ReviewRequestUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.review.dto.response.ReviewRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.review.service.ReviewRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/review-requests")
@RequiredArgsConstructor
public class ReviewRequestController {

    private final ReviewRequestService service;
    private final ReviewRequestMapper mapper;

    @PostMapping
    public ResponseEntity<ReviewRequestModel> create(
            @Valid @RequestBody ReviewRequestCreateRequestDto dto
            ){
        ReviewRequest entity = service.create(mapper.toServiceDto(dto));
        ReviewRequestResponseDto response = mapper.toResponseDto(entity);

        WebMvcLinkBuilder listLink = linkTo(ReviewRequestController.class);
        WebMvcLinkBuilder selfLink = listLink.slash(response.getId());

        ReviewRequestModel model = new ReviewRequestModel(response);
        model.add(selfLink.withSelfRel());

        return ResponseEntity
                .created(selfLink.toUri())
                .body(model);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewRequestResponseDto> getOne(@PathVariable UUID id) {

        return ResponseEntity
                .ok(mapper.toResponseDto(service.findRequestById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<ReviewRequestResponseDto>> getList(
            @PageableDefault(size = 20, sort = "createdOn", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        return ResponseEntity
                .ok(service.findAllRequests(pageable).map(mapper::toResponseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewRequestResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewRequestUpdateRequestDto dto
    ) {
        return ResponseEntity
                .ok(mapper.toResponseDto(service.updateRequest(id, mapper.toServiceDto(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteRequestById(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/images")
    public String uploadImages(
            @RequestBody MultipartFile image
    ) throws IOException {
        return service.uploadImage(image);
    }
}
