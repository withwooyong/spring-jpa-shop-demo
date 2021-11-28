package com.spring.shopdemo.exception;

/**
 * @author WOOYONG
 * @since 2021-11-26
 */
public class OutOfStockException extends RuntimeException {
  public OutOfStockException(String message) {
    super(message);
  }

}
