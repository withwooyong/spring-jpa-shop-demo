package com.spring.shopdemo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Data
public class ItemDto {

  private Long id;
  private String itemNm;
  private Integer price;
  private String itemDetail;
  private String sellStatCd;
  private LocalDateTime regTime;
  private LocalDateTime updateTime;

}
