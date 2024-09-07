package com.ebsolutions.spring.junit;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@SpringBootTest
@AutoConfigureMockMvc
@Import(BaseTestConfiguration.class)
public class BaseTestContext {
    protected final String tableName = "SERVICES_SPRING_JUNIT_LOCAL";

    @MockBean
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

}
