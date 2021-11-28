package com.spring.shopdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Configuration
@EnableJpaAuditing
@Slf4j
public class AuditConfig {
  @Bean
  public AuditorAware<String> auditorProvider() {
    log.debug("auditorProvider");
    return new AuditorAwareImpl();
  }
}
