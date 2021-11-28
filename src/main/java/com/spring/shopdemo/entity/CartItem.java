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
@Table(name = "cart_item")
@Entity
public class CartItem extends BaseEntity {
  @Id
  @GeneratedValue
  @Column(name = "cart_item_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  @ToString.Exclude
  private Cart cart;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  @ToString.Exclude
  private Item item;

  private int count;

  public static CartItem createCartItem(Cart cart, Item item, int count) {
    CartItem cartItem = new CartItem();
    cartItem.setCart(cart);
    cartItem.setItem(item);
    cartItem.setCount(count);
    return cartItem;
  }

  public void addCount(int count) {
    this.count += count;
  }

  public void updateCount(int count) {
    this.count = count;
  }
}
