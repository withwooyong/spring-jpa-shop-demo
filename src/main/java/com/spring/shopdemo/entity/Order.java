package com.spring.shopdemo.entity;

import com.spring.shopdemo.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "order")
public class Order extends BaseEntity {
  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  @ToString.Exclude
  private Member member;

  private LocalDateTime orderDate; //주문일

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus; //주문상태

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<OrderItem> orderItems = new ArrayList<>();

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  public static Order createOrder(Member member, List<OrderItem> orderItemList) {
    Order order = new Order();
    order.setMember(member);

    for (OrderItem orderItem : orderItemList) {
      order.addOrderItem(orderItem);
    }

    order.setOrderStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());
    return order;
  }

  public int getTotalPrice() {
    int totalPrice = 0;
    for (OrderItem orderItem : orderItems) {
      totalPrice += orderItem.getTotalPrice();
    }
    return totalPrice;
  }

  public void cancelOrder() {
    this.orderStatus = OrderStatus.CANCEL;
    for (OrderItem orderItem : orderItems) {
      orderItem.cancel();
    }
  }
}
