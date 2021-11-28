package com.spring.shopdemo.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Data
public class MainItemDto {

  private Long id;
  private String itemNm;
  private String itemDetail;
  private String imgUrl;
  private Integer price;

  @QueryProjection
  public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
    this.id = id;
    this.itemNm = itemNm;
    this.itemDetail = itemDetail;
    this.imgUrl = imgUrl;
    this.price = price;
  }
}
