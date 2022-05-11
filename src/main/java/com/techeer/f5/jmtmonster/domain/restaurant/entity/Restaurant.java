package com.techeer.f5.jmtmonster.domain.restaurant.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Table(name = "Restaurant")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    // Todo 휴대폰 번호, 주소 등 추가 정보 저장이 필요할 시 Column 추가
    // Todo 특히 주소에 대한 엔티티 고민해봐야 함

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @NotNull
    private Long cid;

    private String name;

    private Long x_cord;
    private Long y_cord;

}
