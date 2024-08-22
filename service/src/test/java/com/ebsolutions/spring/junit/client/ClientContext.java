package com.ebsolutions.spring.junit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import static org.mockito.Mockito.mock;

//@SpringBootTest
//@AutoConfigureMockMvc
public class ClientContext {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ClientController clientController;

    @Autowired
    protected DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        System.out.println("You are here");
        return mock(DynamoDbEnhancedClient.class);
    }

    ;
}
