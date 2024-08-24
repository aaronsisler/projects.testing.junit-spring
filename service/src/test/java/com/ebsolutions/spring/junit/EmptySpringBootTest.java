package com.ebsolutions.spring.junit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class EmptySpringBootTest extends BaseTestContext {

    @Autowired
    private SpringJunitServiceApplication application;

    @Test
    void contextLoads() {
        assertThat(application).isNotNull();
    }
}
