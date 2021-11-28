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
@Table(name = "cart")
@Entity
public class Cart extends BaseEntity {
  @Id
  @Column(name = "cart_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  @ToString.Exclude
  private Member member;

  public static Cart createCart(Member member) {
    Cart cart = new Cart();
    cart.setMember(member);
    return cart;
  }
}
