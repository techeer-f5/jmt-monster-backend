package com.techeer.f5.jmtmonster.domain.oauth.domain;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
public class PersistentToken {
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "uuid4", strategy = "uuid4")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;
}
