package com.techeer.f5.jmtmonster.domain.user.domain;

import com.techeer.f5.jmtmonster.domain.home.domain.HomeToUser;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@ToString(of = {"id", "email"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Size(min = 1, max = 30, message = "이름 길이는 1자부터 30자까지 가능합니다.")
    @NotBlank
    @Column(unique = true)
    private String name;

    @Size(min = 1, max = 100, message = "이메일 길이는 1자부터 30자까지 가능합니다.")
    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @Size(min = 1, max = 30, message = "닉네임 길이는 1자부터 30자까지 가능합니다.")
    @Nullable
    @Builder.Default
    @Setter
    private String nickname = null;

    @Size(min = 1, max = 1024, message = "주소 길이는 1자부터 1024자까지 가능합니다.")
    @Nullable
    @Builder.Default
    @Setter
    private String address = null;

    @Size(min = 1, max = 4096, message = "이미지 주소 길이는 1자부터 1024자까지 가능합니다.")
    @Nullable
    @Builder.Default
    @Setter
    private String imageUrl = null;

    @NotNull
    @Builder.Default
    @Setter
    private Boolean emailVerified = false;

    @NotNull
    @Builder.Default
    @Setter
    private Boolean extraInfoInjected = false;

    @NotNull
    @Builder.Default
    @Setter
    private Boolean verified = false;

    // List<T>는 여러개의 대응을 가지는 타입
    // mappedBy는 자기 자신의 테이블 이름
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private List<PersistentToken> tokens = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private List<HomeToUser> homeToUsers = new ArrayList<>();

    @Transactional
    public void addToken(PersistentToken persistentToken) {
        tokens.add(persistentToken);
    }

    public boolean addExtraInfo(String nickname, String address, String imageUrl)
            throws IllegalStateException {
        this.nickname = nickname;
        this.address = address;
        this.imageUrl = imageUrl;

        extraInfoInjected = true;

        verify();

        return true;
    }

    public boolean verifyEmail() throws IllegalStateException {
        if (emailVerified) {
            throw new IllegalStateException("이메일이 이미 인증되었습니다.");
        }

        emailVerified = true;

        verify();

        return true;
    }

    private boolean verify() {
        boolean verified = emailVerified && extraInfoInjected;

        this.verified = verified;

        return verified;
    }
}
