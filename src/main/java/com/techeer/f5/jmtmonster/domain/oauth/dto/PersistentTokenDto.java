package com.techeer.f5.jmtmonster.domain.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersistentTokenDto {
    @Nullable
    private UUID id;
}
