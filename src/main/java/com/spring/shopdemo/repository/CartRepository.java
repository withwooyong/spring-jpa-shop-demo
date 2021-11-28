package com.spring.shopdemo.repository;

import com.spring.shopdemo.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

  Cart findByMemberId(Long memberId);
}
