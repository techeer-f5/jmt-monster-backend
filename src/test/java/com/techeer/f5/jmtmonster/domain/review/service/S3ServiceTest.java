package com.techeer.f5.jmtmonster.domain.review.service;

import com.techeer.f5.jmtmonster.s3.util.S3ManagerImpl;
import org.assertj.core.api.BDDAssumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"test"})
public class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private S3ManagerImpl s3Manager;

    private MockMultipartFile mockMultipartFile;
    private String filename;
    private String expectedUrl;

    @BeforeEach
    void setUp(){
        filename = "mock.png";
        mockMultipartFile = new MockMultipartFile("file", filename,
                "image/png", "test data".getBytes());
        expectedUrl = "Test URL";
    }

    @Test
    @DisplayName("upLoadImageTest")
    void upLoadImageTest() throws IOException {
        // given
        given(s3Service.uploadImage(mockMultipartFile)).willReturn(expectedUrl);

        // when
        String actualUrl = s3Manager.uploadImage(mockMultipartFile);

        // then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("deleteByURLTest")
    void deleteByURLTest() {
        // given
        given(s3Service.deleteByURL(expectedUrl)).willReturn(expectedUrl);

        // when
        String actualUrl = s3Manager.deleteByURL(expectedUrl);

        // then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("updateByURLTest")
    void updateByURLTest() throws IOException {
        // given
        String newFilename = "mock.png";
        MockMultipartFile newMockMultipartFile = new MockMultipartFile("file", newFilename,
                "image/png", "test data".getBytes());
        String oldUrl = "Test URL";
        String expectedUrl = "Test URL updated";
        given(s3Manager.updateByURL(newMockMultipartFile,oldUrl)).willReturn(expectedUrl);

        // when
        String actualUrl = s3Manager.updateByURL(newMockMultipartFile,oldUrl);

        // then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }



}
