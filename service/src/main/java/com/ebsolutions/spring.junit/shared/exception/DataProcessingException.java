package com.ebsolutions.spring.junit.shared.exception;


public class DataProcessingException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public DataProcessingException(String errorMessage) {
    super(errorMessage);
  }

  public DataProcessingException(String errorMessage, Exception e) {
    super(errorMessage, e);
  }
}