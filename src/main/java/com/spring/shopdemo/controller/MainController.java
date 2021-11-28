package com.spring.shopdemo.controller;

import com.spring.shopdemo.dto.ItemSearchDto;
import com.spring.shopdemo.dto.MainItemDto;
import com.spring.shopdemo.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@RequiredArgsConstructor
@Controller
@Slf4j
public class MainController {

  private final ItemService itemService;

  @GetMapping(value = "/")
  public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
    log.debug("################ main itemSearchDto={} page={} model={}", itemSearchDto.toString(), page, model.toString());
    System.out.println("main itemSearchDto={}");
    log.debug("################ main itemSearchDto={} page={} model={}", itemSearchDto.toString(), page, model.toString());
    Pageable pageable = PageRequest.of(page.orElse(0), 6);
    Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

    model.addAttribute("items", items);
    model.addAttribute("itemSearchDto", itemSearchDto);
    model.addAttribute("maxPage", 5);

    return "main";
  }
}
