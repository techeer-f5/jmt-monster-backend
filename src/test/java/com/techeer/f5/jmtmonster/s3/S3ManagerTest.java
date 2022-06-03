package com.techeer.f5.jmtmonster.s3;

import com.techeer.f5.jmtmonster.s3.util.S3Manager;
import io.findify.s3mock.S3Mock;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"test"})
@Import(S3MockConfig.class)
public class S3ManagerTest {

    @Autowired
    S3Manager s3Manager;

    @Autowired
    S3Mock s3Mock;

    private MockMultipartFile mockMultipartFile;
    private String file;

    @BeforeEach
    void setUp(){
        file = "mock1.png";
        mockMultipartFile = new MockMultipartFile("file", file,
                "image/png", "test data".getBytes());
    }

    @After("Test is done")
    public void shutdownMockS3(){
        s3Mock.stop();
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
    void S3ManagerTest() throws IOException {
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
