package com.ebsolutions.spring.junit.client;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.ebsolutions.spring.junit.BaseTestContext;
import com.ebsolutions.spring.junit.Constants;
import com.ebsolutions.spring.junit.shared.SortKeyType;
import com.ebsolutions.spring.junit.shared.exception.DataProcessingException;
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


public class ClientReadAllTest extends BaseTestContext {
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

  private final LocalDateTime now = LocalDateTime.now();


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
  void givenServerErrorOccursWhenGetClientsIsCalledShouldReturnCorrectResponse() throws Exception {
    Mockito.when(clientDtoDynamoDbTable.query(ArgumentMatchers.any(QueryConditional.class)))
        .thenThrow(DataProcessingException.class);

    this.mockMvc.perform(get(Constants.CLIENTS_URL))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError());
  }

  //  @Test
  void givenClientsExistWhenGetClientsIsCalledShouldReturnCorrectResponse() throws Exception {
    ClientDto firstClientDto = ClientDto.builder()
        .name("First Client Name")
        .partitionKey(SortKeyType.CLIENT.name())
        .sortKey("firstClientSortKey")
        .createdOn(now)
        .lastUpdatedOn(now)
        .build();

    clientDtos.add(firstClientDto);
    Mockito.when(clientDtoStream.toList()).thenReturn(clientDtos);

    this.mockMvc.perform(get(Constants.CLIENTS_URL))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$[0].name", is("First Client Name")))
        .andExpect(jsonPath("$[0].clientId", is("firstClientSortKey")))
        .andExpect(jsonPath("$[0].createdOn", is(now.toString())))
        .andExpect(jsonPath("$[0].lastUpdatedOn", is(now.toString())));
  }
}
