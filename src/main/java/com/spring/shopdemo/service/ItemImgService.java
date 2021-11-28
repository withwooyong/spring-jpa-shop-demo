package com.spring.shopdemo.service;

import com.spring.shopdemo.entity.ItemImg;
import com.spring.shopdemo.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ItemImgService {

  @Value("${itemImgLocation}")
  private String itemImgLocation;

  private final ItemImgRepository itemImgRepository;

  private final FileService fileService;

  public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
    log.debug("saveItemImg itemImg={}", itemImg.toString());
    String oriImgName = itemImgFile.getOriginalFilename();
    String imgName = "";
    String imgUrl = "";

    log.debug("itemImgLocation={} oriImgName={}", itemImgLocation, oriImgName);
    //파일 업로드
    if (!StringUtils.isEmpty(oriImgName)) {
      log.debug("########## 1");
      imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
      log.debug("########## 2");
      imgUrl = itemImgLocation + "/" + imgName;
      log.debug("imgName={} imgUrl={}", imgName, imgUrl);
//      imgUrl = "/images/item/" + imgName;
    }

    //상품 이미지 정보 저장
    itemImg.updateItemImg(oriImgName, imgName, imgUrl);
    itemImgRepository.save(itemImg);
  }

  public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
    log.debug("updateItemImg itemImgId={}", itemImgId);
    if (!itemImgFile.isEmpty()) {
      ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
              .orElseThrow(EntityNotFoundException::new);

      //기존 이미지 파일 삭제
      if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
        fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
      }

      String oriImgName = itemImgFile.getOriginalFilename();
      Assert.notNull(oriImgName, "oriImgName must not be null");
      String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
      String imgUrl = "/images/item/" + imgName;
      savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
    }
  }
}
