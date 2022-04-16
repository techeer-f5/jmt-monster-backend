package com.techeer.f5.jmtmonster.domain.crawling.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id = UUID.randomUUID();

    @NotNull // OneToMany
    private Long rest_id;

    @NotNull
    private String name; // menu 이름
    private String price; // 가격
    private String picture; // 사진

}
