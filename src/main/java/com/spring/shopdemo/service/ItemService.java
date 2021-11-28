package com.spring.shopdemo.service;

import com.spring.shopdemo.dto.ItemFormDto;
import com.spring.shopdemo.dto.ItemImgDto;
import com.spring.shopdemo.dto.ItemSearchDto;
import com.spring.shopdemo.dto.MainItemDto;
import com.spring.shopdemo.entity.Item;
import com.spring.shopdemo.entity.ItemImg;
import com.spring.shopdemo.repository.ItemImgRepository;
import com.spring.shopdemo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ItemService {

  private final ItemRepository itemRepository;

  private final ItemImgService itemImgService;

  private final ItemImgRepository itemImgRepository;

  public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
    log.debug("saveItem itemFormDto={}", itemFormDto);
    //상품 등록
    Item item = itemFormDto.createItem();
    itemRepository.save(item);

    //이미지 등록
    for (int i = 0; i < itemImgFileList.size(); i++) {
      ItemImg itemImg = new ItemImg();
      itemImg.setItem(item);

      if (i == 0)
        itemImg.setRepImgYn("Y");
      else
        itemImg.setRepImgYn("N");

      itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
    }
    return item.getId();
  }

  @Transactional(readOnly = true)
  public ItemFormDto getItemDtl(Long itemId) {
    log.debug("getItemDtl itemId={}", itemId);
    List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
    List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    for (ItemImg itemImg : itemImgList) {
      ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
      itemImgDtoList.add(itemImgDto);
    }

    Item item = itemRepository.findById(itemId)
            .orElseThrow(EntityNotFoundException::new);
    ItemFormDto itemFormDto = ItemFormDto.of(item);
    itemFormDto.setItemImgDtoList(itemImgDtoList);
    return itemFormDto;
  }

  public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
    log.debug("updateItem itemFormDto={}", itemFormDto.toString());
    //상품 수정
    Item item = itemRepository.findById(itemFormDto.getId())
            .orElseThrow(EntityNotFoundException::new);
    item.updateItem(itemFormDto);
    List<Long> itemImgIds = itemFormDto.getItemImgIds();

    //이미지 등록
    for (int i = 0; i < itemImgFileList.size(); i++) {
      itemImgService.updateItemImg(itemImgIds.get(i),
              itemImgFileList.get(i));
    }

    return item.getId();
  }

  @Transactional(readOnly = true)
  public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
    log.debug("getAdminItemPage itemSearchDto={} pageable={}", itemSearchDto.toString(), pageable.getPageNumber());
    return itemRepository.getAdminItemPage(itemSearchDto, pageable);
  }

  @Transactional(readOnly = true)
  public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
    log.debug("getMainItemPage itemSearchDto={} pageable={}", itemSearchDto.toString(), pageable.getPageNumber());
    return itemRepository.getMainItemPage(itemSearchDto, pageable);
  }
}
