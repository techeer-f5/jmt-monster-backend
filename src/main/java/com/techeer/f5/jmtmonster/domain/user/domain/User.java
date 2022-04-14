package com.techeer.f5.jmtmonster.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techeer.f5.jmtmonster.domain.oauth.domain.PersistentToken;
import com.techeer.f5.jmtmonster.domain.user.domain.AuthProvider;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;


    @Size(min = 1, max = 30, message = "이름 길이는 1자부터 30자까지 가능합니다.")
    @NotBlank
    private String name;


    @Size(min = 1, max = 100, message = "이메일 길이는 1자부터 30자까지 가능합니다.")
    @Email
    @NotNull
    private String email;


    @Size(min = 1, max = 3, message = "이메일 길이는 1자부터 30자까지 가능합니다.")
    @Nullable
    private String nickname = null;


    @Size(min = 1, max = 1024, message = "주소 길이는 1자부터 1024자까지 가능합니다.")
    @Nullable
    @Column(name="address_")
    private String address = null;

    @Size(min = 1, max = 4096, message = "이미지 주소 길이는 1자부터 1024자까지 가능합니다.")
    @Nullable
    private String imageUrl = null;

    @NotNull
    @Builder.Default
    private Boolean emailVerified = false;

    @NotNull
    @Builder.Default
    private Boolean extraInfoInjected = false;

    @NotNull
    @Builder.Default
    private Boolean verified = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @OneToMany
    private List<PersistentToken> tokens;

    public boolean addExtraInfo(String nickname, String address) throws IllegalStateException {
        if (extraInfoInjected) {
            throw new IllegalStateException("사용자 추가 정보가 이미 입력되었습니다.");
        }

        this.nickname = nickname;
        this.address = address;

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
