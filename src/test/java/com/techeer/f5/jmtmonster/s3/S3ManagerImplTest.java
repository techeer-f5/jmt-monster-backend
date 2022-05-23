package com.techeer.f5.jmtmonster.s3;

import com.techeer.f5.jmtmonster.s3.util.S3ManagerImpl;
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
public class S3ManagerImplTest {

    @Autowired
    S3ManagerImpl s3Manager;

    @Autowired
    S3Mock s3Mock;

    private MockMultipartFile mockMultipartFile;
    private String expectedFilename;

    @BeforeEach
    void setUp(){
        expectedFilename = "mock1.png";
        mockMultipartFile = new MockMultipartFile("file", expectedFilename,
                "image/png", "test data".getBytes());
    }

    @After("Test is done")
    public void shutdownMockS3(){
        s3Mock.stop();
    }
    
    @Test
    @DisplayName("uploadTest")
    void uploadTest() throws IOException {
        // given

        // when
        String url = s3Manager.uploadImage(mockMultipartFile);

        // then
        assertThat(url.contains(expectedFilename)).isTrue();
    }

    @Test
    @DisplayName("deleteByURLTest")
    void deleteByURLTest() throws IOException {
        // given
        String expectedUrl = s3Manager.uploadImage(mockMultipartFile);

        // when
        String actualUrl = s3Manager.deleteByURL(expectedUrl);

        // then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }
    
    @Test
    @DisplayName("")
    void updateByURLTest() throws IOException {
        // given
        String expectedFilename2 = "mock2.png";
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("file", expectedFilename2,
                "image/png", "test data".getBytes());
        
        // when
        String oldUrl = s3Manager.uploadImage(mockMultipartFile);
        String newUrl = s3Manager.updateByURL(mockMultipartFile2,oldUrl);
        
        // then
        assertThat(newUrl.contains(expectedFilename2)).isTrue();
    }
    



}
