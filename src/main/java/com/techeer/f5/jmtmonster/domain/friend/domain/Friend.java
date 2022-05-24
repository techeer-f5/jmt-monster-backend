package com.techeer.f5.jmtmonster.domain.friend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @NotNull
    private User fromUser;

    @ManyToOne
    @NotNull
    private User toUser;

    @NotNull
    @JsonProperty("isHangingOut")
    private boolean isHangingOut;

    public void update(User fromUser, User toUser, boolean isHangingOut) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.isHangingOut = isHangingOut;
    }
}
