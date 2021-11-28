package com.spring.shopdemo.service;

import com.spring.shopdemo.entity.Member;
import com.spring.shopdemo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;

  public Member saveMember(Member member) {
    log.debug("saveMember member={}", member.toString());
    validateDuplicateMember(member);
    return memberRepository.save(member);
  }

  private void validateDuplicateMember(Member member) {
    log.debug("validateDuplicateMember member={}", member.toString());
    Member findMember = memberRepository.findByEmail(member.getEmail());
    if (findMember != null) {
      throw new IllegalStateException("이미 가입된 회원입니다.");
    }
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.debug("loadUserByUsername email={}", email);
    Member member = memberRepository.findByEmail(email);

    if (member == null) {
      throw new UsernameNotFoundException(email);
    }
    return User.builder()
            .username(member.getEmail())
            .password(member.getPassword())
            .roles(member.getRole().toString())
            .build();
  }
}
