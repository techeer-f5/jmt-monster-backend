package com.techeer.f5.jmtmonster.domain.home.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
// There's no such thing as updating raw history,
// So history implies that it is read-only, response only.
public class HomeHistoriesDto {

    @Builder.Default
    private List<HomeHistoryDto> history = new ArrayList<>();

}
