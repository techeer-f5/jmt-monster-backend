package com.techeer.f5.jmtmonster.domain.review.service;

import com.techeer.f5.jmtmonster.s3.util.S3Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test"})
public class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private S3Manager s3Manager;

    private String givenURL;
    private String givenFileName;
    private MockMultipartFile mockMultipartFile;
    private String file;

    @BeforeEach
    void setUp() throws IOException {
        file = "mock1.png";
        mockMultipartFile = new MockMultipartFile("file", file,
                "image/png", "test data".getBytes());
        givenURL = "Test URL";
        givenFileName = "Test File Name";
        given(s3Manager.upload(any(),any())).willReturn(givenURL);
        doNothing().when(s3Manager).delete(givenFileName);
        // Mocking한 메소드는 디폴트로 void 메소드지만 명시.
    }

    @Test
    @DisplayName("이미지 업로드 테스트")
    void uploadImageTest() throws IOException {
        // given

        // when
        String actualURL = s3Service.uploadImage(mockMultipartFile);

        // then
        assertThat(actualURL).isEqualTo(givenURL);
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
    void deleteImageTest() {
        // given

        // when
        String actualFileName = s3Service.deleteImage(givenFileName);

        // then
        assertThat(actualFileName).isEqualTo(givenFileName);
    }

    @Test
    @DisplayName("이미지 업데이트 테스트")
    void deleteAndCreateImageTest() throws IOException {
        // given

        // when
        String actualURL = s3Service.deleteAndCreateImage(mockMultipartFile, givenFileName);

        // then
        assertThat(actualURL).isEqualTo(givenURL);
    }


}
