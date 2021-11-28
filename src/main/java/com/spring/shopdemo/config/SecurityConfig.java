package com.spring.shopdemo.config;

import com.spring.shopdemo.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  MemberService memberService;

  @Value("${itemImgLocation}")
  private String itemImgLocation;

  @Bean
  public PasswordEncoder passwordEncoder() {
    log.debug("passwordEncoder");
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    log.debug("configure(HttpSecurity http)");
    http.formLogin()
            .loginPage("/members/login")
            .defaultSuccessUrl("/")
            .usernameParameter("email")
            .failureUrl("/members/login/error")
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
            .logoutSuccessUrl("/");

    http.authorizeRequests()
            .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
            .mvcMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated();

    http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    log.debug("configure(AuthenticationManagerBuilder auth)");
    auth.userDetailsService(memberService)
            .passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    log.debug("configure(WebSecurity web)");
    web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    web.ignoring().antMatchers(itemImgLocation);
    web.ignoring().antMatchers("/thymeleaf/**");
  }

}
