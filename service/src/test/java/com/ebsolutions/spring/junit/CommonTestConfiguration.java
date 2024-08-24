package com.ebsolutions.spring.junit;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@TestConfiguration
public class CommonTestConfiguration {
    @MockBean
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;
}
