package com.ebsolutions.spring.junit.client;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.spring.junit.BaseTestContext;
import com.ebsolutions.spring.junit.Constants;
import com.ebsolutions.spring.junit.shared.SortKeyType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;


public class ClientTest extends BaseTestContext {
  @Autowired
  private MockMvc mockMvc;

  @Mock
  private DynamoDbTable<ClientDto> clientDtoDynamoDbTable;

  @Mock
  private PageIterable<ClientDto> pageIterable;

  @Mock
  private SdkIterable<ClientDto> sdkIterableItems;

  @Mock
  private Stream<ClientDto> clientDtoStream;

  private List<ClientDto> clientDtos = new ArrayList<>();


  @BeforeEach
  void setup() {
    Mockito.when(dynamoDbEnhancedClient.table(Mockito.eq(databaseConfig.getTableName()),
        ArgumentMatchers.<TableSchema<ClientDto>>any())).thenReturn(clientDtoDynamoDbTable);
    Mockito.when(clientDtoDynamoDbTable.query(ArgumentMatchers.any(QueryConditional.class)))
        .thenReturn(pageIterable);
    Mockito.when(pageIterable.items()).thenReturn(sdkIterableItems);
    Mockito.when(sdkIterableItems.stream()).thenReturn(clientDtoStream);
  }

  @Test
  void givenClientsDoNotExistWhenGetClientsIsCalledShouldReturnCorrectResponse() throws Exception {
    this.clientDtos = Collections.emptyList();
    Mockito.when(clientDtoStream.toList()).thenReturn(clientDtos);

    this.mockMvc.perform(get(Constants.CLIENTS_URL))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  void givenClientsExistWhenGetClientsIsCalledShouldReturnCorrectResponse() throws Exception {
    ClientDto firstClientDto = ClientDto.builder()
        .name("First Client Name")
        .partitionKey(SortKeyType.CLIENT.name())
        .sortKey("firstClientSortKey")
        .createdOn(LocalDateTime.now())
        .lastUpdatedOn(LocalDateTime.now())
        .build();

    clientDtos.add(firstClientDto);
    Mockito.when(clientDtoStream.toList()).thenReturn(clientDtos);

    this.mockMvc.perform(get(Constants.CLIENTS_URL))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
