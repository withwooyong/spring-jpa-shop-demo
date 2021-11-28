package com.spring.shopdemo.dto;

import com.spring.shopdemo.entity.OrderItem;
import lombok.Data;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Data
public class OrderItemDto {

  public OrderItemDto(OrderItem orderItem, String imgUrl) {
    this.itemNm = orderItem.getItem().getItemNm();
    this.count = orderItem.getCount();
    this.orderPrice = orderItem.getOrderPrice();
    this.imgUrl = imgUrl;
  }

  private String itemNm; //상품명
  private int count; //주문 수량

  private int orderPrice; //주문 금액
  private String imgUrl; //상품 이미지 경로
}
