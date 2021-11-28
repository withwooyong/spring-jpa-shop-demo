package com.spring.shopdemo.dto;

import com.spring.shopdemo.constant.ItemSellStatus;
import lombok.Data;
import lombok.ToString;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Data
public class ItemSearchDto {

  private String searchDateType;
  private ItemSellStatus searchSellStatus;
  private String searchBy;
  private String searchQuery = "";

}
