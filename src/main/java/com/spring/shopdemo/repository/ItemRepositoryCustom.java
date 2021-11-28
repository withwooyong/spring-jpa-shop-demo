package com.spring.shopdemo.repository;

import com.spring.shopdemo.dto.ItemSearchDto;
import com.spring.shopdemo.dto.MainItemDto;
import com.spring.shopdemo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
public interface ItemRepositoryCustom {
  Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

  Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
