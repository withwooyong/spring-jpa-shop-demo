package com.spring.shopdemo.controller;

import com.spring.shopdemo.dto.OrderDto;
import com.spring.shopdemo.dto.OrderHistDto;
import com.spring.shopdemo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderService orderService;

  @PostMapping(value = "/order")
  @ResponseBody
  public Object order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {
    log.debug("order orderDto={} principal={}", orderDto.toString(), principal);
    if (bindingResult.hasErrors()) {
      StringBuilder sb = new StringBuilder();
      List<FieldError> fieldErrors = bindingResult.getFieldErrors();

      for (FieldError fieldError : fieldErrors) {
        sb.append(fieldError.getDefaultMessage());
      }

      return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
    }

    String email = principal.getName();
    Long orderId;

    try {
      orderId = orderService.order(orderDto, email);
    } catch (Exception e) {
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<Long>(orderId, HttpStatus.OK);
  }

  @GetMapping(value = {"/orders", "/orders/{page}"})
  public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
    log.debug("orderHist page={} principal={} model={}", page, principal, model.toString());
    Pageable pageable = PageRequest.of(page.orElse(0), 4);
    Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

    model.addAttribute("orders", ordersHistDtoList);
    model.addAttribute("page", pageable.getPageNumber());
    model.addAttribute("maxPage", 5);

    return "order/orderHist";
  }

  @PostMapping("/order/{orderId}/cancel")
  @ResponseBody
  public Object cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
    log.debug("cancelOrder orderId={} principal={}", orderId, principal);
    if (!orderService.validateOrder(orderId, principal.getName())) {
      return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
    orderService.cancelOrder(orderId);
    return new ResponseEntity<Long>(orderId, HttpStatus.OK);
  }
}
