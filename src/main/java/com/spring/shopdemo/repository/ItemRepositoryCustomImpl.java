package com.spring.shopdemo.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.shopdemo.constant.ItemSellStatus;
import com.spring.shopdemo.dto.ItemSearchDto;
import com.spring.shopdemo.dto.MainItemDto;
import com.spring.shopdemo.dto.QMainItemDto;
import com.spring.shopdemo.entity.Item;
import com.spring.shopdemo.entity.QItem;
import com.spring.shopdemo.entity.QItemImg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Slf4j
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public ItemRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
    log.debug("searchSellStatusEq searchSellStatus={}", searchSellStatus.toString());
    return QItem.item.itemSellStatus.eq(searchSellStatus);
  }

  private BooleanExpression regDtsAfter(String searchDateType) {
    log.debug("regDtsAfter searchDateType={}", searchDateType);
    LocalDateTime dateTime = LocalDateTime.now();

    if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
      return null;
    } else if (StringUtils.equals("1d", searchDateType)) {
      dateTime = dateTime.minusDays(1);
    } else if (StringUtils.equals("1w", searchDateType)) {
      dateTime = dateTime.minusWeeks(1);
    } else if (StringUtils.equals("1m", searchDateType)) {
      dateTime = dateTime.minusMonths(1);
    } else if (StringUtils.equals("6m", searchDateType)) {
      dateTime = dateTime.minusMonths(6);
    }

    return QItem.item.regTime.after(dateTime);
  }

  private BooleanExpression searchByLike(String searchBy, String searchQuery) {
    log.debug("searchByLike searchBy={} searchQuery={}", searchBy, searchQuery);
    if (StringUtils.equals("itemNm", searchBy)) {
      return QItem.item.itemNm.like("%" + searchQuery + "%");
    } else if (StringUtils.equals("createdBy", searchBy)) {
      return QItem.item.createdBy.like("%" + searchQuery + "%");
    }

    return null;
  }

  @Override
  public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
    log.debug("getAdminItemPage itemSearchDto={} pageable={}", itemSearchDto.toString(), pageable.getPageNumber());
    List<Item> contents = queryFactory
            .selectFrom(QItem.item)
            .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                    searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                    searchByLike(itemSearchDto.getSearchBy(),
                            itemSearchDto.getSearchQuery()))
            .orderBy(QItem.item.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    return new PageImpl<>(contents, pageable, contents.size());
  }

  private BooleanExpression itemNmLike(String searchQuery) {
    log.debug("itemNmLike searchQuery={}", searchQuery);
    return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
  }

  @Override
  public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
    log.debug("getMainItemPage itemSearchDto={} pageable={}", itemSearchDto.toString(), pageable.getPageNumber());
    QItem item = QItem.item;
    QItemImg itemImg = QItemImg.itemImg;

    List<MainItemDto> contents = queryFactory
            .select(
                    new QMainItemDto(
                            item.id,
                            item.itemNm,
                            item.itemDetail,
                            itemImg.imgUrl,
                            item.price)
            )
            .from(itemImg)
            .join(itemImg.item, item)
            .where(itemImg.repImgYn.eq("Y"))
            .where(itemNmLike(itemSearchDto.getSearchQuery()))
            .orderBy(item.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    return new PageImpl<>(contents, pageable, contents.size());
  }
}
