package com.spring.shopdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@AllArgsConstructor
@Data
public class CartDetailDto {

  private Long cartItemId; //장바구니 상품 아이디
  private String itemNm; //상품명
  private int price; //상품 금액
  private int count; //수량
  private String imgUrl; //상품 이미지 경로

}
