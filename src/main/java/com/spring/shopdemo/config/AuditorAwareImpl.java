package com.spring.shopdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    log.debug("getCurrentAuditor");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = "";
    if (authentication != null) {
      userId = authentication.getName();
    }
    return Optional.of(userId);
  }
}
