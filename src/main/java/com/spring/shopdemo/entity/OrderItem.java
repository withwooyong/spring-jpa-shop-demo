package com.spring.shopdemo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Getter
@Setter
@ToString
@Table(name = "order_item")
@Entity
public class OrderItem extends BaseEntity {
  @Id
  @GeneratedValue
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  @ToString.Exclude
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  @ToString.Exclude
  private Order order;

  private int orderPrice; //주문가격

  private int count; //수량

  public static OrderItem createOrderItem(Item item, int count) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(item);
    orderItem.setCount(count);
    orderItem.setOrderPrice(item.getPrice());
    item.removeStock(count);
    return orderItem;
  }

  public int getTotalPrice() {
    return orderPrice * count;
  }

  public void cancel() {
    this.getItem().addStock(count);
  }
}
