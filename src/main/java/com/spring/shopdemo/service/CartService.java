package com.spring.shopdemo.service;

import com.spring.shopdemo.dto.CartDetailDto;
import com.spring.shopdemo.dto.CartItemDto;
import com.spring.shopdemo.dto.CartOrderDto;
import com.spring.shopdemo.dto.OrderDto;
import com.spring.shopdemo.entity.Cart;
import com.spring.shopdemo.entity.CartItem;
import com.spring.shopdemo.entity.Item;
import com.spring.shopdemo.entity.Member;
import com.spring.shopdemo.repository.CartItemRepository;
import com.spring.shopdemo.repository.CartRepository;
import com.spring.shopdemo.repository.ItemRepository;
import com.spring.shopdemo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CartService {

  private final ItemRepository itemRepository;
  private final MemberRepository memberRepository;
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final OrderService orderService;

  public Long addCart(CartItemDto cartItemDto, String email) {
    log.debug("addCart cartItemDto={} email={}", cartItemDto.toString(), email);

    Item item = itemRepository.findById(cartItemDto.getItemId())
            .orElseThrow(EntityNotFoundException::new);
    Member member = memberRepository.findByEmail(email);

    Cart cart = cartRepository.findByMemberId(member.getId());
    if (cart == null) {
      cart = Cart.createCart(member);
      cartRepository.save(cart);
    }

    CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

    if (savedCartItem != null) {
      savedCartItem.addCount(cartItemDto.getCount());
      return savedCartItem.getId();
    } else {
      CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
      cartItemRepository.save(cartItem);
      return cartItem.getId();
    }
  }

  @Transactional(readOnly = true)
  public List<CartDetailDto> getCartList(String email) {
    log.debug("getCartList email={}", email);
    List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

    Member member = memberRepository.findByEmail(email);
    Cart cart = cartRepository.findByMemberId(member.getId());
    if (cart == null) {
      return cartDetailDtoList;
    }

    cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
    return cartDetailDtoList;
  }

  @Transactional(readOnly = true)
  public boolean validateCartItem(Long cartItemId, String email) {
    log.debug("validateCartItem cartItemId={} email={}", cartItemId, email);
    Member curMember = memberRepository.findByEmail(email);
    CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(EntityNotFoundException::new);
    Member savedMember = cartItem.getCart().getMember();
    return StringUtils.equals(curMember.getEmail(), savedMember.getEmail());
  }

  public void updateCartItemCount(Long cartItemId, int count) {
    log.debug("updateCartItemCount cartItemId={} count={}", cartItemId, count);
    CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(EntityNotFoundException::new);

    cartItem.updateCount(count);
  }

  public void deleteCartItem(Long cartItemId) {
    log.debug("deleteCartItem cartItemId={}", cartItemId);
    CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(EntityNotFoundException::new);
    cartItemRepository.delete(cartItem);
  }

  public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
    log.debug("orderCartItem cartOrderDtoList={} email={}", cartOrderDtoList.toString(), email);
    List<OrderDto> orderDtoList = new ArrayList<>();

    for (CartOrderDto cartOrderDto : cartOrderDtoList) {
      CartItem cartItem = cartItemRepository
              .findById(cartOrderDto.getCartItemId())
              .orElseThrow(EntityNotFoundException::new);

      OrderDto orderDto = new OrderDto();
      orderDto.setItemId(cartItem.getItem().getId());
      orderDto.setCount(cartItem.getCount());
      orderDtoList.add(orderDto);
    }

    Long orderId = orderService.orders(orderDtoList, email);
    for (CartOrderDto cartOrderDto : cartOrderDtoList) {
      CartItem cartItem = cartItemRepository
              .findById(cartOrderDto.getCartItemId())
              .orElseThrow(EntityNotFoundException::new);
      cartItemRepository.delete(cartItem);
    }
    return orderId;
  }
}
