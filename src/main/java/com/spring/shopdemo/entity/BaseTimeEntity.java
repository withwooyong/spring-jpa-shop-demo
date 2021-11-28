package com.spring.shopdemo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
@Getter
@Setter
@ToString
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
public abstract class BaseTimeEntity {
  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime regTime;

  @LastModifiedDate
  private LocalDateTime updateTime;
}
