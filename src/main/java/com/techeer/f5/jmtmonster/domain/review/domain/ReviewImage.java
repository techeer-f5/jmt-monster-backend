package com.techeer.f5.jmtmonster.domain.review.domain;

import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Review review;

    @NotNull
    private String url;

    public void update(String url){
        this.url = url;
    }

    @Override
    public String toString() {
        return "ReviewFood{" +
                "id=" + id +
                ", review id=" + review.getId() +
                ", url=" + url +
                '}';
    }
}
