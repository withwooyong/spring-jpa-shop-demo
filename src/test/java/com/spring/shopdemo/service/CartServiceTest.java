package com.spring.shopdemo.service;

import com.spring.shopdemo.constant.ItemSellStatus;
import com.spring.shopdemo.dto.CartItemDto;
import com.spring.shopdemo.entity.CartItem;
import com.spring.shopdemo.entity.Item;
import com.spring.shopdemo.entity.Member;
import com.spring.shopdemo.repository.CartItemRepository;
import com.spring.shopdemo.repository.ItemRepository;
import com.spring.shopdemo.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
class CartServiceTest {

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  CartService cartService;

  @Autowired
  CartItemRepository cartItemRepository;

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

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  @DisplayName("장바구니 담기 테스트")
  void addCart() {
    Item item = saveItem();
    Member member = saveMember();

    CartItemDto cartItemDto = new CartItemDto();
    cartItemDto.setCount(5);
    cartItemDto.setItemId(item.getId());

    Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
    CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(EntityNotFoundException::new);

    assertEquals(item.getId(), cartItem.getItem().getId());
    assertEquals(cartItemDto.getCount(), cartItem.getCount());
  }

  @Test
  void getCartList() {
  }

  @Test
  void validateCartItem() {
  }

  @Test
  void updateCartItemCount() {
  }

  @Test
  void deleteCartItem() {
  }

  @Test
  void orderCartItem() {
  }
}