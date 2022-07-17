package com.techeer.f5.jmtmonster.domain.review.domain;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;

import java.util.ArrayList;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Setter
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @NotNull
    @ManyToOne
    private User user;

    @Column(columnDefinition = "TEXT") // 장문 텍스트
    private String content;

    @NotNull
    @Column(name = "review_like")
    private Like like;

    @NotNull
    private Star star;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    private List<ReviewFood> foodList = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    private List<ReviewImage> imageList = new ArrayList<>();

    public void addFoodList(List<ReviewFood> foodList) {
        this.foodList.addAll(foodList);
    }

    public void addImageList(List<ReviewImage> imageList) {
        this.imageList.addAll(imageList);
    }

}
