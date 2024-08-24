package com.ebsolutions.spring.junit;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(CommonTestConfiguration.class)
public class BaseTestContext {
}
