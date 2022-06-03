package com.techeer.f5.jmtmonster.domain.friend.domain;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniqueFriendFromUserAndToUser",
                columnNames = {"from_user_id", "to_user_id"})})
public class Friend extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private User toUser;

    @NotNull
    private boolean isHangingOut;

    @Builder
    public Friend(User fromUser, User toUser, boolean isHangingOut) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.isHangingOut = isHangingOut;
    }

    public void update(User fromUser, User toUser, boolean isHangingOut) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.isHangingOut = isHangingOut;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", fromUser.email=" + fromUser.getEmail() +
                ", toUser.email=" + toUser.getEmail() +
                ", isHangingOut=" + isHangingOut +
                '}';
    }
}
