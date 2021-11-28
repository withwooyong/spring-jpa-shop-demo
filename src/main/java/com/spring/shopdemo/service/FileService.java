package com.spring.shopdemo.service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.Lob;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Service
@Slf4j
public class FileService {

  public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
    log.debug("uploadFile uploadPath={} originalFileName={}", uploadPath, originalFileName);
    UUID uuid = UUID.randomUUID();
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    String savedFileName = uuid.toString() + extension;
    String fileUploadFullUrl = uploadPath + "/" + savedFileName;

    System.out.println("### " + uploadPath);
    System.out.println("### " + savedFileName);
    System.out.println("### " + fileUploadFullUrl);
    try {
      FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
      fos.write(fileData);
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return savedFileName;
  }

  public void deleteFile(String filePath) throws Exception {
    log.debug("deleteFile filePath={}", filePath);
    File deleteFile = new File(filePath);
    if (deleteFile.exists()) {
      if (deleteFile.delete())
        log.info("파일을 삭제하였습니다.");
      else {
        log.info("파일 삭제를 실패하였습니다.");
      }
    } else {
      log.info("파일이 존재하지 않습니다.");
    }
  }
}
