package com.ebsolutions.spring.junit;

import com.ebsolutions.spring.junit.config.DatabaseConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@SpringBootTest
@AutoConfigureMockMvc
@Import(BaseTestConfiguration.class)
public class BaseTestContext {
  public ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  public DynamoDbEnhancedClient dynamoDbEnhancedClient;

  @Autowired
  public DatabaseConfig databaseConfig;
}
