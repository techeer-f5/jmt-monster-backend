package com.techeer.f5.jmtmonster.domain.home.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class HomeHistoryDto {

    @Builder.Default
    private String name = "";

    @Builder.Default
    private String code = "";

    @Builder.Default
    @Setter
    private Boolean isCurrentHome = Boolean.FALSE;

}
