package com.spring.shopdemo.service;

import com.spring.shopdemo.constant.ItemSellStatus;
import com.spring.shopdemo.constant.OrderStatus;
import com.spring.shopdemo.dto.OrderDto;
import com.spring.shopdemo.entity.Item;
import com.spring.shopdemo.entity.Member;
import com.spring.shopdemo.entity.Order;
import com.spring.shopdemo.entity.OrderItem;
import com.spring.shopdemo.repository.ItemRepository;
import com.spring.shopdemo.repository.MemberRepository;
import com.spring.shopdemo.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
class OrderServiceTest {

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  MemberRepository memberRepository;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  public Item saveItem() {
    Item item = new Item();
    item.setItemNm("테스트 상품");
    item.setPrice(10000);
    item.setItemDetail("테스트 상품 상세 설명");
    item.setItemSellStatus(ItemSellStatus.SELL);
    item.setStockNumber(100);
    return itemRepository.save(item);
  }

  public Member saveMember() {
    Member member = new Member();
    member.setEmail("test@test.com");
    return memberRepository.save(member);
  }

  @Test
  @DisplayName("주문 테스트")
  void order() {
    Item item = saveItem();
    Member member = saveMember();

    OrderDto orderDto = new OrderDto();
    orderDto.setCount(10);
    orderDto.setItemId(item.getId());

    Long orderId = orderService.order(orderDto, member.getEmail());
    Order order = orderRepository.findById(orderId)
            .orElseThrow(EntityNotFoundException::new);

    List<OrderItem> orderItems = order.getOrderItems();

    int totalPrice = orderDto.getCount() * item.getPrice();

    assertEquals(totalPrice, order.getTotalPrice());
  }

  @Test
  void getOrderList() {
  }

  @Test
  void validateOrder() {
  }

  @Test
  @DisplayName("주문 취소 테스트")
  void cancelOrder() {
    Item item = saveItem();
    Member member = saveMember();

    OrderDto orderDto = new OrderDto();
    orderDto.setCount(10);
    orderDto.setItemId(item.getId());
    Long orderId = orderService.order(orderDto, member.getEmail());

    Order order = orderRepository.findById(orderId)
            .orElseThrow(EntityNotFoundException::new);
    orderService.cancelOrder(orderId);

    assertEquals(OrderStatus.CANCEL, order.getOrderStatus());
    assertEquals(100, item.getStockNumber());
  }

  @Test
  void orders() {
  }
}