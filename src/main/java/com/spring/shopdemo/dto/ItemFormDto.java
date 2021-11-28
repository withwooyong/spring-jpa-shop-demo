package com.spring.shopdemo.dto;

import com.spring.shopdemo.constant.ItemSellStatus;
import com.spring.shopdemo.entity.Item;
import lombok.Data;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Data
public class ItemFormDto {
  private Long id;

  @NotBlank(message = "상품명은 필수 입력 값입니다.")
  private String itemNm;

  @NotNull(message = "가격은 필수 입력 값입니다.")
  private Integer price;

  @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
  private String itemDetail;

  @NotNull(message = "재고는 필수 입력 값입니다.")
  private Integer stockNumber;

  private ItemSellStatus itemSellStatus;

  private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

  private List<Long> itemImgIds = new ArrayList<>();



  private static ModelMapper modelMapper = new ModelMapper();

  public Item createItem() {
    return modelMapper.map(this, Item.class);
  }

  public static ItemFormDto of(Item item) {
    return modelMapper.map(item, ItemFormDto.class);
  }
}
