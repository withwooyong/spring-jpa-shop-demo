package com.spring.shopdemo.repository;

import com.spring.shopdemo.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

  List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

  ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn);
}
