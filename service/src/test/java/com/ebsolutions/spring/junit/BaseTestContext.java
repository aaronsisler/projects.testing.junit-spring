package com.ebsolutions.spring.junit;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureMockMvc
@Import(CommonTestConfiguration.class)
public class BaseTestContext {
}
