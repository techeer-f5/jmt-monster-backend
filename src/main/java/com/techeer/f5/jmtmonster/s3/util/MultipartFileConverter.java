package com.techeer.f5.jmtmonster.s3.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class MultipartFileConverter {

    // 로컬에 파일 업로드 하기
    public Optional<File> toFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("s3-upload-", multipartFile.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (Exception exception) {
            return Optional.empty();
        }

        return Optional.of(file);
    }

    // 로컬에 저장된 이미지 지우기
    public void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }
}
