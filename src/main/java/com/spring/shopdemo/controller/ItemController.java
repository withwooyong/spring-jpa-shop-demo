package com.spring.shopdemo.controller;

import com.spring.shopdemo.dto.ItemFormDto;
import com.spring.shopdemo.dto.ItemSearchDto;
import com.spring.shopdemo.entity.Item;
import com.spring.shopdemo.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

  private final ItemService itemService;

  @GetMapping(value = "/admin/item/new")
  public String itemForm(Model model) {
    log.debug("itemForm model={}", model.toString());
    model.addAttribute("itemFormDto", new ItemFormDto());
    return "item/itemForm";
  }

  @PostMapping(value = "/admin/item/new")
  public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                        Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
    log.debug("itemNew itemFormDto={}", itemFormDto.toString());
    if (bindingResult.hasErrors()) {
      return "item/itemForm";
    }

    if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
      model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
      return "item/itemForm";
    }

    try {
      Long saveItemId = itemService.saveItem(itemFormDto, itemImgFileList);
      log.debug("saveItemId={}", saveItemId);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
      return "item/itemForm";
    }
    return "redirect:/";
  }

  @GetMapping(value = "/admin/item/{itemId}")
  public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
    log.debug("itemDtl itemId={} model={}", itemId, model.toString());
    try {
      ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
      model.addAttribute("itemFormDto", itemFormDto);
    } catch (EntityNotFoundException e) {
      model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
      model.addAttribute("itemFormDto", new ItemFormDto());
      return "item/itemForm";
    }

    return "item/itemForm";
  }

  @PostMapping(value = "/admin/item/{itemId}")
  public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                           @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
    log.debug("itemUpdate itemFormDto={} itemImgFileList={}", itemFormDto.toString(), itemImgFileList.toString());
    if (bindingResult.hasErrors()) {
      return "item/itemForm";
    }

    if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
      model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
      return "item/itemForm";
    }

    try {
      Long updateItemId = itemService.updateItem(itemFormDto, itemImgFileList);
      log.debug("updateItemId={}", updateItemId);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
      return "item/itemForm";
    }

    return "redirect:/";
  }

  @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
  public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
    log.debug("itemManage itemSearchDto={}", itemSearchDto.toString());
    Pageable pageable = PageRequest.of(page.orElse(0), 3);
    Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

    model.addAttribute("items", items);
    model.addAttribute("itemSearchDto", itemSearchDto);
    model.addAttribute("maxPage", 5);

    return "item/itemMng";
  }

  @GetMapping(value = "/item/{itemId}")
  public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
    log.debug("itemDtl model={} itemId={}", model.toString(), itemId);
    ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
    model.addAttribute("item", itemFormDto);
    return "item/itemDtl";
  }
}
