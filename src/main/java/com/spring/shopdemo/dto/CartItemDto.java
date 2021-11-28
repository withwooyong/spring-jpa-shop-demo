package com.spring.shopdemo.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Data
public class CartItemDto {
  @NotNull(message = "상품 아이디는 필수 입력 값 입니다.")
  private Long itemId;

  @Min(value = 1, message = "최소 1개 이상 담아주세요")
  private int count;
}
