package com.techeer.f5.jmtmonster.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.techeer.f5.jmtmonster.s3.util.S3Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/** 주의 **/
// 컨테이너를 실행하려면 아래의 명령어로 컨테이너를 먼저 띄어야 합니다.
// docker run --rm -it -p 4566:4566 -p 4571:4571 localstack/localstack
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackS3Config.class)
public class S3ManagerTest {

    @Autowired
    S3Manager s3Manager;

    @Autowired
    AmazonS3 amazonS3;

    private MockMultipartFile mockMultipartFile;
    private String file;

    @BeforeEach
    void setUp(){
        file = "mock1.png";
        mockMultipartFile = new MockMultipartFile("file", file,
                "image/png", "test data".getBytes());

        String bucketName = "jmt-monster-bucket";
        amazonS3.createBucket(bucketName);
    }

    @Test
    @DisplayName("S3 이미지 업로드 테스트")
    void uploadTest() throws IOException {
        // given

        // when
        String actualURL = s3Manager.upload(mockMultipartFile, s3Manager.getDIR_NAME());

        // then
        assertThat(actualURL).contains("mock1.png");

    }

    @Test
    @DisplayName("S3 이미지 삭제 테스트")
    void deleteTest() throws IOException {
        // given
        String file2 = "mock2.png";
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("file", file,
                "image/png", "test data".getBytes());

        // when
        String actualURL = s3Manager.upload(mockMultipartFile, s3Manager.getDIR_NAME());
        String actualFilename = actualURL.substring(actualURL.length()-20,actualURL.length());

        // then
        s3Manager.delete(actualFilename);
    }
}
