package com.spring.shopdemo.repository;

import com.spring.shopdemo.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-25
 */
public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

  List<Item> findByItemNm(String itemNm);
  List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
  List<Item> findByPriceLessThan(Integer price);
  List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

  @Query("select i from Item i where i.itemDetail like " +
          "%:itemDetail% order by i.price desc")
  List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

  @Query(value="select * from item i where i.item_detail like " +
          "%:itemDetail% order by i.price desc", nativeQuery = true)
  List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
