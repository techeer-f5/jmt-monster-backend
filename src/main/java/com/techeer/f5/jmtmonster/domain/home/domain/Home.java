package com.techeer.f5.jmtmonster.domain.home.domain;

import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Home extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "home")
    @Builder.Default
    private List<HomeToUser> homeToUsers = new ArrayList<>();

    @Builder.Default
    private String code = "";

    @Builder.Default
    private String name = "";
}
