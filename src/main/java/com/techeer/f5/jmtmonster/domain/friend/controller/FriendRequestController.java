package com.techeer.f5.jmtmonster.domain.friend.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.techeer.f5.jmtmonster.domain.friend.domain.FriendRequest;
import com.techeer.f5.jmtmonster.domain.friend.dto.mapper.FriendRequestMapper;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestCreateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestModel;
import com.techeer.f5.jmtmonster.domain.friend.dto.request.FriendRequestUpdateRequestDto;
import com.techeer.f5.jmtmonster.domain.friend.dto.response.FriendRequestResponseDto;
import com.techeer.f5.jmtmonster.domain.friend.service.FriendService;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendService service;
    private final FriendRequestMapper mapper;

    // TODO: add authorization with given user identity
    // TODO: add HATEOAS and hopefully apply interface & generic

    @PostMapping
    public ResponseEntity<FriendRequestModel> create(
            @Valid @RequestBody FriendRequestCreateRequestDto dto
    ) {
        FriendRequest entity = service.createRequest(mapper.toServiceDto(dto));
        FriendRequestResponseDto response = mapper.toResponseDto(entity);

        WebMvcLinkBuilder listLink = linkTo(FriendRequestController.class);
        WebMvcLinkBuilder selfLink = listLink.slash(response.getId());

        FriendRequestModel model = new FriendRequestModel(response);
        model.add(listLink.withRel("list"));
        model.add(selfLink.withSelfRel());
        model.add(selfLink.withRel("update"));
        model.add(selfLink.withRel("delete"));

        return ResponseEntity
                .created(selfLink.toUri())
                .body(model);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FriendRequestResponseDto> getOne(@PathVariable UUID id) {

        return ResponseEntity
                .ok(mapper.toResponseDto(service.findRequestById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<FriendRequestResponseDto>> getList(
            @PageableDefault(size = 20, sort = "createdOn", direction = Direction.DESC) final Pageable pageable
    ) {
        return ResponseEntity
                .ok(service.findAllRequests(pageable).map(mapper::toResponseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FriendRequestResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody FriendRequestUpdateRequestDto dto
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
}
