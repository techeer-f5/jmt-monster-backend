package com.techeer.f5.jmtmonster.domain.crawling.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull // OneToMany
    private Long rest_id;

    @NotNull
    private String name; // menu 이름
    private String price; // 가격
    private String picture; // 사진

}
