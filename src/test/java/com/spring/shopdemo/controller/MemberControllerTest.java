package com.spring.shopdemo.controller;

import com.spring.shopdemo.dto.MemberFormDto;
import com.spring.shopdemo.entity.Member;
import com.spring.shopdemo.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MemberService memberService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  public void createMember(String email, String password) {
    MemberFormDto memberFormDto = new MemberFormDto();
    memberFormDto.setEmail(email);
    memberFormDto.setName("홍길동");
    memberFormDto.setAddress("서울시 마포구 합정동");
    memberFormDto.setPassword(password);
    Member member = Member.createMember(memberFormDto, passwordEncoder);
    memberService.saveMember(member);
  }

  @Test
  void memberForm() {
  }

  @Test
  void newMember() {
  }

  @Test
  @DisplayName("로그인 성공 테스트")
  void loginMember() throws Exception {
    String email = "test@email.com";
    String password = "1234";
    this.createMember(email, password);
    mockMvc.perform(formLogin().userParameter("email")
                    .loginProcessingUrl("/members/login")
                    .user(email).password(password))
            .andExpect(SecurityMockMvcResultMatchers.authenticated());
  }

  @Test
  @DisplayName("로그인 실패 테스트")
  void loginError() throws Exception {
    String email = "test@email.com";
    String password = "1234";
    this.createMember(email, password);
    mockMvc.perform(formLogin().userParameter("email")
                    .loginProcessingUrl("/members/login")
                    .user(email).password("12345"))
            .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
  }
}