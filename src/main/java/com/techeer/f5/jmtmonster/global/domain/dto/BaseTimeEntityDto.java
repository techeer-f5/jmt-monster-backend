package com.techeer.f5.jmtmonster.global.domain.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

public abstract class BaseTimeEntityDto {

    @NotNull
    private LocalDateTime createdOn;

    @NotNull
    private LocalDateTime updatedOn;
}
