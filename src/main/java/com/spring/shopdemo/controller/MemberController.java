package com.spring.shopdemo.controller;

import com.spring.shopdemo.dto.MemberFormDto;
import com.spring.shopdemo.entity.Member;
import com.spring.shopdemo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
@Slf4j
public class MemberController {

  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;

  @GetMapping(value = "/new")
  public String memberForm(Model model) {
    log.debug("memberForm model={}", model.toString());
    model.addAttribute("memberFormDto", new MemberFormDto());
    return "member/memberForm";
  }

  @PostMapping(value = "/new")
  public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
    log.debug("newMember memberFormDto={}", memberFormDto.toString());
    if (bindingResult.hasErrors()) {
      return "member/memberForm";
    }

    try {
      Member member = Member.createMember(memberFormDto, passwordEncoder);
      memberService.saveMember(member);
    } catch (IllegalStateException e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "member/memberForm";
    }

    return "redirect:/";
  }

  @GetMapping(value = "/login")
  public String loginMember() {
    log.debug("loginMember");
    return "/member/memberLoginForm";
  }

  @GetMapping(value = "/login/error")
  public String loginError(Model model) {
    log.debug("loginError model={}", model.toString());
    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
    return "/member/memberLoginForm";
  }
}
