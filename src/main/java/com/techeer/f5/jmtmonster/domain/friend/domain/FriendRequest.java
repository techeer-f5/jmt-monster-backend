package com.techeer.f5.jmtmonster.domain.friend.domain;

import com.techeer.f5.jmtmonster.domain.user.domain.User;
import com.techeer.f5.jmtmonster.global.domain.domain.BaseTimeEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniqueFromUserAndToUser",
                columnNames = {"from_user_id", "to_user_id"})})
public class FriendRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @ManyToOne(optional = false)
    @NotNull
    private User fromUser;

    @ManyToOne(optional = false)
    @NotNull
    private User toUser;

    @NotNull
    @Column(length = 16)
    @Convert(converter = FriendRequestStatusConverter.class)
    private FriendRequestStatus status;

    public void update(User fromUser, User toUser, FriendRequestStatus status) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = status;
    }
}
