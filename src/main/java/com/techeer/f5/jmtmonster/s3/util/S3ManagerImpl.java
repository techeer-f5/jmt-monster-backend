package com.techeer.f5.jmtmonster.s3.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.techeer.f5.jmtmonster.domain.review.dao.ReviewImageRepository;
import com.techeer.f5.jmtmonster.domain.review.domain.Updatable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ManagerImpl implements S3Manager {

    @Getter
    private final String directoryName = "JMT-Review-Image";
    private final AmazonS3Client amazonS3Client;

    private final MultipartFileConverter multipartFileConverter;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    @Override
    public String upload(MultipartFile multipartFile, Optional<String> dirName) throws IOException {
        File uploadFile = multipartFileConverter.toFile(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile, dirName);
    }

    // S3로 파일 업로드하기
    @Override
    public String upload(File uploadFile, Optional<String> optionalDirectoryName) {
        String dirName = getDirectoryName();

        if (optionalDirectoryName.isPresent()) {
            dirName += "/" + optionalDirectoryName.get();
        }

        String fileName = uploadFile.getName();

        String nanoId = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, "1234567890".toCharArray(), 10);
        fileName = String.format("%s-%s", fileName, nanoId);

        fileName = String.format("%s/%s", dirName, fileName);  // S3에 저장된 파일 이름

        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        multipartFileConverter.removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    @Override
    public String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // Upload Image
    @Override
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        File uploadFile = multipartFileConverter.toFile(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        String url = upload(uploadFile, Optional.empty());

        return url;
    }

    @Override
    public String deleteByURL(String filename) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, filename);
        amazonS3Client.deleteObject(request);
        return filename;
    }

    @Override
    public String updateByURL(MultipartFile multipartFile, Updatable<String> updatable) throws IOException {
        String prevUrl = updatable.getColumn();

        String newUrl = upload(multipartFile, Optional.empty());

        updatable.update(newUrl);

        deleteByURL(prevUrl);

        return newUrl;
    }
}