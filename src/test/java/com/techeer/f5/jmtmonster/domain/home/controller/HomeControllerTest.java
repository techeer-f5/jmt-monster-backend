package com.techeer.f5.jmtmonster.domain.home.controller;

import com.techeer.f5.jmtmonster.domain.oauth.repository.PersistentTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
// For use test db
@ActiveProfiles(profiles = {"test"})
public class HomeControllerTest {

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Test
    @DisplayName("토큰 없이 현재 집 조회 - 인증 에러로 인한 실패 (실패)")
    @Transactional(readOnly = true)
    public void getHomeWithoutToken() {

        // MockMvc로 인증 없이 리퀘스트 전송

        // Aspect가 인증 관련 에러처리, 에러 리스폰스 받고 핸들링

        // FIXME: implementation needed
        assertThat(true).isFalse();

    }

    @Test
    @DisplayName("토큰 주입 후 데이터 없이 현재 집 조회 - 404 에러로 인한 실패 (실패)")
    @Transactional(readOnly = true)
    public void getHomeWithoutHomeData() {

        // 사용자 생성

        // PersistentTokenRepository로 토큰 주입

        // MockMvc로 토큰 헤더에 넣어서 리퀘스트 전송

        // 404 리스폰스 (집 데이터가 없음)

        // FIXME: implementation needed
        assertThat(true).isFalse();
    }

    @Test
    @DisplayName("토큰, 데이터 주입 후 현재 집 조회 - 정렬된 제일 최근 집 리턴 (성공)")
    @Transactional(readOnly = true)
    public void getHome() {

        // 사용자 생성

        // PersistentTokenRepository로 토큰 주입

        // 집 여러개 migration api로 생성

        // MockMvc로 토큰 헤더에 넣어서 리퀘스트 전송

        // 200 리스폰스 & 마지막 집 데이터

        // FIXME: implementation needed
        assertThat(true).isFalse();

    }

    @Test
    @DisplayName("토큰, 데이터 주입 후 거주 히스토리 조회 - 최근 집이 먼저 오게 정렬된 거주 히스토리 리턴 (성공)")
    @Transactional(readOnly = true)
    public void getHistory() {

        // 사용자 생성

        // PersistentTokenRepository로 토큰 주입

        // 집 여러개 migration api로 생성

        // MockMvc로 토큰 헤더에 넣어서 리퀘스트 전송

        // 200 리스폰스 & 이사 히스토리 정렬된 데이터 받음

        // FIXME: implementation needed
        assertThat(true).isFalse();

    }

}
