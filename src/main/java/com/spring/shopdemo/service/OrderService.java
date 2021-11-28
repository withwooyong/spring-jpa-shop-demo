package com.spring.shopdemo.service;

import com.spring.shopdemo.dto.OrderDto;
import com.spring.shopdemo.dto.OrderHistDto;
import com.spring.shopdemo.dto.OrderItemDto;
import com.spring.shopdemo.entity.*;
import com.spring.shopdemo.repository.ItemImgRepository;
import com.spring.shopdemo.repository.ItemRepository;
import com.spring.shopdemo.repository.MemberRepository;
import com.spring.shopdemo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class OrderService {

  private final ItemRepository itemRepository;

  private final MemberRepository memberRepository;

  private final OrderRepository orderRepository;

  private final ItemImgRepository itemImgRepository;

  public Long order(OrderDto orderDto, String email) {
    log.debug("order orderDto={} email={}", orderDto.toString(), email);
    Item item = itemRepository.findById(orderDto.getItemId())
            .orElseThrow(EntityNotFoundException::new);

    Member member = memberRepository.findByEmail(email);

    List<OrderItem> orderItemList = new ArrayList<>();
    OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
    orderItemList.add(orderItem);
    Order order = Order.createOrder(member, orderItemList);
    orderRepository.save(order);

    return order.getId();
  }

  @Transactional(readOnly = true)
  public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
    log.debug("getOrderList email={} pageable={}", email, pageable.getPageNumber());
    List<Order> orders = orderRepository.findOrders(email, pageable);
    Long totalCount = orderRepository.countOrder(email);

    List<OrderHistDto> orderHistDtos = new ArrayList<>();

    for (Order order : orders) {
      OrderHistDto orderHistDto = new OrderHistDto(order);
      List<OrderItem> orderItems = order.getOrderItems();
      for (OrderItem orderItem : orderItems) {
        ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn
                (orderItem.getItem().getId(), "Y");
        OrderItemDto orderItemDto =
                new OrderItemDto(orderItem, itemImg.getImgUrl());
        orderHistDto.addOrderItemDto(orderItemDto);
      }

      orderHistDtos.add(orderHistDto);
    }

    return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
  }

  @Transactional(readOnly = true)
  public boolean validateOrder(Long orderId, String email) {
    log.debug("validateOrder orderId={} email={}", orderId, email);
    Member curMember = memberRepository.findByEmail(email);
    Order order = orderRepository.findById(orderId)
            .orElseThrow(EntityNotFoundException::new);
    Member savedMember = order.getMember();

    return StringUtils.equals(curMember.getEmail(), savedMember.getEmail());
  }

  public void cancelOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(EntityNotFoundException::new);
    order.cancelOrder();
  }

  public Long orders(List<OrderDto> orderDtoList, String email) {
    log.debug("orders orderDtoList={} email={}", orderDtoList.toString(), email);
    Member member = memberRepository.findByEmail(email);
    List<OrderItem> orderItemList = new ArrayList<>();

    for (OrderDto orderDto : orderDtoList) {
      Item item = itemRepository.findById(orderDto.getItemId())
              .orElseThrow(EntityNotFoundException::new);

      OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
      orderItemList.add(orderItem);
    }

    Order order = Order.createOrder(member, orderItemList);
    orderRepository.save(order);

    return order.getId();
  }
}
