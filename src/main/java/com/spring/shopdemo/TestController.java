package com.spring.shopdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WOOYONG
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/test")
public class TestController {
  @GetMapping(value = "/test")
  public UserDto test() {
    UserDto userDto = new UserDto();
    userDto.setName("dragon");
    userDto.setAge(20);
    return userDto;
  }
}
