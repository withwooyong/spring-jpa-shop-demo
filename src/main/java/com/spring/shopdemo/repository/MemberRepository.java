package com.spring.shopdemo.repository;

import com.spring.shopdemo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */

public interface MemberRepository extends JpaRepository<Member, Long> {

  Member findByEmail(String email);

}
