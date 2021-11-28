package com.spring.shopdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${uploadPath}")
  String uploadPath;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    log.debug("addResourceHandlers");
    registry.addResourceHandler("/images/**")
            .addResourceLocations(uploadPath);
  }
}
