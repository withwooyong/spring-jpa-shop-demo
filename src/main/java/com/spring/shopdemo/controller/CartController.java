package com.spring.shopdemo.controller;

import com.spring.shopdemo.dto.CartDetailDto;
import com.spring.shopdemo.dto.CartItemDto;
import com.spring.shopdemo.dto.CartOrderDto;
import com.spring.shopdemo.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@RequiredArgsConstructor
@Controller
@Slf4j
public class CartController {

  private final CartService cartService;

  @PostMapping(value = "/cart")
  @ResponseBody
  public Object order(@RequestBody @Valid CartItemDto cartItemDto,
                      BindingResult bindingResult, Principal principal) {

    log.debug("order cartItemDto={}", cartItemDto.toString());
    if (bindingResult.hasErrors()) {
      StringBuilder sb = new StringBuilder();
      List<FieldError> fieldErrors = bindingResult.getFieldErrors();

      for (FieldError fieldError : fieldErrors) {
        sb.append(fieldError.getDefaultMessage());
      }

      return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
    }

    String email = principal.getName();
    Long cartItemId;

    try {
      cartItemId = cartService.addCart(cartItemDto, email);
    } catch (Exception e) {
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
  }

  @GetMapping(value = "/cart")
  public String orderHist(Principal principal, Model model) {
    log.debug("orderHist principal={} model={}", principal.toString(), model.toString());
    List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
    model.addAttribute("cartItems", cartDetailList);
    return "cart/cartList";
  }

  @PatchMapping(value = "/cartItem/{cartItemId}")
  @ResponseBody
  public Object updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {
    log.debug("updateCartItem cartItemId={} count={} principal={}", cartItemId, count, principal);
    if (count <= 0) {
      return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
    } else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
      return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    cartService.updateCartItemCount(cartItemId, count);
    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
  }

  @DeleteMapping(value = "/cartItem/{cartItemId}")
  @ResponseBody
  public Object deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
    log.debug("deleteCartItem cartItemId={} principal={}", cartItemId, principal);
    if (!cartService.validateCartItem(cartItemId, principal.getName())) {
      return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    cartService.deleteCartItem(cartItemId);
    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
  }

  @PostMapping(value = "/cart/orders")
  @ResponseBody
  public Object orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal) {
    log.debug("orderCartItem cartOrderDto={} principal={}", cartOrderDto.toString(), principal);
    List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

    if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
      return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
    }

    for (CartOrderDto cartOrder : cartOrderDtoList) {
      if (!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())) {
        return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
      }
    }

    Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
    return new ResponseEntity<Long>(orderId, HttpStatus.OK);
  }
}
