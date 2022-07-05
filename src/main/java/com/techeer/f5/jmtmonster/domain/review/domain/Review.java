package com.techeer.f5.jmtmonster.domain.review.domain;


import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
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

//    @NotNull
    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<ReviewFood> foodList;

//    @NotNull
    @OneToMany(mappedBy = "review", orphanRemoval = true)
    private List<ReviewImage> imageList;

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", content=" + content +
                ", like=" + like +
                ", star=" + star +
                ", foodList size=" + foodList.size() + // 순환 참조 발생해서 개수 반환
                ", imageList size=" + imageList.size() +
                '}';
    }
}
