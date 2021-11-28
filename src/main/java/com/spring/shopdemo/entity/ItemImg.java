package com.spring.shopdemo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Getter
@Setter
@ToString
@Table(name = "item_img")
@Entity
public class ItemImg extends BaseEntity {

  @Id
  @Column(name = "item_img_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String imgName; //이미지 파일명

  private String oriImgName; //원본 이미지 파일명

  private String imgUrl; //이미지 조회 경로

  private String repImgYn; //대표 이미지 여부

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  @ToString.Exclude
  private Item item;

  public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
    this.oriImgName = oriImgName;
    this.imgName = imgName;
    this.imgUrl = imgUrl;
  }
}
