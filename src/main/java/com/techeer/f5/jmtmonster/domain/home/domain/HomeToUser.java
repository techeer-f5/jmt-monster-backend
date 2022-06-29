package com.techeer.f5.jmtmonster.domain.home.domain;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeToUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    @Builder.Default
    private Home home = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    @Builder.Default
    private User user = null;

    @Builder.Default
    @Setter
    private boolean current = false;
}
