package com.techeer.f5.jmtmonster.domain.review.domain;


import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequest extends BaseTimeEntity {

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
    private Like like;

    @NotNull
    private Star star;

    @NotNull
    @OneToMany(mappedBy = "user")
    private List<ReviewFood> foodList;

    @NotNull
    @OneToMany(mappedBy = "user")
    private List<ReviewImage> imageList;

    public void update(String content, Like like, Star star){
        this.content = content;
        this.like = like;
        this.star = star;
    }
}
