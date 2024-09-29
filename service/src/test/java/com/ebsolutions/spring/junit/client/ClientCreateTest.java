package com.ebsolutions.spring.junit.client;

import com.ebsolutions.spring.junit.BaseTestContext;
import com.ebsolutions.spring.junit.Constants;
import com.ebsolutions.spring.junit.util.ComparisonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableMetadata;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;


public class ClientCreateTest extends BaseTestContext {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ComparisonUtil comparisonUtil;

  @Mock
  private DynamoDbTable<ClientDto> clientDtoDynamoDbTable;

  @Mock
  public TableSchema<ClientDto> clientDtoTableSchema;

  @Mock
  public TableMetadata tableMetadata;

  private final List<ClientDto> clientDtos = new ArrayList<>();

  private final LocalDateTime now = LocalDateTime.now();


  @BeforeEach
  void setup() {
    objectMapper.findAndRegisterModules();

    Mockito.when(dynamoDbEnhancedClient.table(Mockito.eq(databaseConfig.getTableName()),
        ArgumentMatchers.<TableSchema<ClientDto>>any())).thenReturn(clientDtoDynamoDbTable);

    Mockito.when(clientDtoDynamoDbTable.tableName()).thenReturn(databaseConfig.getTableName());
    Mockito.when(clientDtoDynamoDbTable.tableSchema()).thenReturn(clientDtoTableSchema);
    Mockito.when(clientDtoTableSchema.tableMetadata()).thenReturn(tableMetadata);
  }

  @Test
  void givenClientIsValidWhenPostClientsIsCalledShouldReturnCorrectResponse() throws Exception {

    ObjectNode submittedFirstClient = objectMapper.createObjectNode();
    submittedFirstClient.put("name", "First Client Name");

    ObjectNode submittedSecondClient = objectMapper.createObjectNode();
    submittedSecondClient.put("name", "Second Client Name");

    ArrayNode jsonNodes = objectMapper.createArrayNode();

    jsonNodes.add(submittedFirstClient);
    jsonNodes.add(submittedSecondClient);

    this.mockMvc.perform(
            MockMvcRequestBuilders.post(Constants.CLIENTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jsonNodes))
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk());

    ArgumentCaptor<ClientDto> clientDtoArgumentCaptor = ArgumentCaptor.forClass(ClientDto.class);
    Mockito.verify(clientDtoTableSchema, Mockito.atLeastOnce())
        .itemToMap(clientDtoArgumentCaptor.capture(), Mockito.anyBoolean());
    List<ClientDto> savedClientDtos = clientDtoArgumentCaptor.getAllValues();


    Assertions.assertEquals(jsonNodes.size(), savedClientDtos.size(),
        "Number of submitted Clients does match the number saved");

    List<String> excludedFields =
        Arrays.asList("createdOn", "lastUpdatedOn", "partitionKey", "sortKey");

    JsonNode savedFirstClient = objectMapper.convertValue(savedClientDtos.get(0), JsonNode.class);
    JsonNode savedSecondClient = objectMapper.convertValue(savedClientDtos.get(1), JsonNode.class);

    Map<String, JsonNode> errorMap;

    errorMap =
        comparisonUtil.compare(submittedFirstClient, savedFirstClient, excludedFields);
    Assertions.assertTrue(errorMap.isEmpty(), "Future checks as to what is wrong");
    errorMap =
        comparisonUtil.compare(submittedSecondClient, savedSecondClient, excludedFields);
    Assertions.assertTrue(errorMap.isEmpty(), "Future checks as to what is wrong");
  }
}
