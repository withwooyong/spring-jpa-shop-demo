package com.spring.shopdemo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Data
public class CartOrderDto {

  private Long cartItemId;
  private List<CartOrderDto> cartOrderDtoList;
}
