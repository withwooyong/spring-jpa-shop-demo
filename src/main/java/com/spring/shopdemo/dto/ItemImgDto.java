package com.spring.shopdemo.dto;

import com.spring.shopdemo.entity.ItemImg;
import lombok.Data;
import org.modelmapper.ModelMapper;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Data
public class ItemImgDto {

  private Long id;
  private String imgName;
  private String oriImgName;
  private String imgUrl;
  private String repImgYn;

  private static ModelMapper modelMapper = new ModelMapper();

  public static ItemImgDto of(ItemImg itemImg) {
    return modelMapper.map(itemImg, ItemImgDto.class);
  }
}
