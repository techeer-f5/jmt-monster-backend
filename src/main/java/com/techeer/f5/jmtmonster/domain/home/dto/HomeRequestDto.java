package com.techeer.f5.jmtmonster.domain.home.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class HomeRequestDto {

    @Builder.Default
    private String name = "";

    @Builder.Default
    private String code = "";

}
