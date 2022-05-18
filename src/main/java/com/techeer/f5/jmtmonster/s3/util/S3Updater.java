package com.techeer.f5.jmtmonster.s3.util;

import com.techeer.f5.jmtmonster.domain.review.domain.Updatable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface S3Updater {
    String updateByURL(MultipartFile multipartFile, Updatable<String> updatable) throws IOException;
}
