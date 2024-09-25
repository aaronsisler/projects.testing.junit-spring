package com.ebsolutions.spring.junit.shared.util;


import java.util.UUID;

public class UniqueIdGenerator {
  public static String generate() {
    return UUID.randomUUID().toString();
  }
}