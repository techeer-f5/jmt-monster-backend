package com.techeer.f5.jmtmonster.s3.util;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface S3Uploader {
    String upload(MultipartFile multipartFile, Optional<String> dirName) throws IOException;
    String upload(File uploadFile, Optional<String> optionalDirectoryName);

    String putS3(File uploadFile, String fileName);
    String uploadImage(MultipartFile multipartFile) throws IOException;

    String getDirectoryName();
}
