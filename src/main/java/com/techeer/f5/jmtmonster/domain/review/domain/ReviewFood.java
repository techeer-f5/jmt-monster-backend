package com.techeer.f5.jmtmonster.domain.review.domain;

import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Setter
@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewFood extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    // @NotNull , 테이블 간의 순환참조를 없애기 위해 NotNull 삭제
    @ManyToOne
    private Review review;

    @NotNull
    private String food;

    public void update(String food){
        this.food = food;
    }
}
