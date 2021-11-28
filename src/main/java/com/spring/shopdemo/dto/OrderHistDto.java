package com.spring.shopdemo.dto;

import com.spring.shopdemo.constant.OrderStatus;
import com.spring.shopdemo.entity.Order;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Data
public class OrderHistDto {

  private Long orderId; //주문아이디
  private String orderDate; //주문날짜
  private OrderStatus orderStatus; //주문 상태
  private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

  public OrderHistDto(Order order) {
    this.orderId = order.getId();
    this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    this.orderStatus = order.getOrderStatus();
  }

  //주문 상품리스트
  public void addOrderItemDto(OrderItemDto orderItemDto) {
    orderItemDtoList.add(orderItemDto);
  }
}
