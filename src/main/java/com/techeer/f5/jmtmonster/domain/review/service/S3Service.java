package com.techeer.f5.jmtmonster.domain.review.service;

import com.techeer.f5.jmtmonster.domain.review.domain.Updatable;
import com.techeer.f5.jmtmonster.s3.util.S3ManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class S3Service {

    private S3ManagerImpl s3Manager;

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        return s3Manager.uploadImage(multipartFile);
    }

    public String deleteByURL(String url){
        return s3Manager.deleteByURL(url);
    }

    public String updateByURL(MultipartFile multipartFile, String url) throws IOException {
        return s3Manager.updateByURL(multipartFile,url);
    }
}
