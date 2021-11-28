package com.spring.shopdemo.repository;

import com.spring.shopdemo.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
