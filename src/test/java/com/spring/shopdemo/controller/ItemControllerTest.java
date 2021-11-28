package com.spring.shopdemo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yml")
class ItemControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("상품 등록 페이지 권한 테스트")
  @WithMockUser(username = "admin", roles = "ADMIN")
  void itemForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
            .andDo(print())
            .andExpect(status().isOk());
  }
}