package com.ebsolutions.spring.junit.client;

import com.ebsolutions.spring.junit.BaseTestContext;
import com.ebsolutions.spring.junit.Constants;
import com.ebsolutions.spring.junit.shared.SortKeyType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    ClientDto firstClientDto = ClientDto.builder()
        .name("First Client Name")
        .partitionKey(SortKeyType.CLIENT.name())
        .sortKey("firstClientSortKey")
        .createdOn(now)
        .lastUpdatedOn(now)
        .build();

    clientDtos.add(firstClientDto);

    this.mockMvc.perform(
            MockMvcRequestBuilders.post(Constants.CLIENTS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDtos))
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk());

    ArgumentCaptor<ClientDto> captor = ArgumentCaptor.forClass(ClientDto.class);

    Mockito.verify(clientDtoTableSchema)
        .itemToMap(captor.capture(), Mockito.anyBoolean());

    ClientDto clientDto = captor.getValue();
    System.out.println("Here");
    // THIS is what we needed and will verify against :-)
    System.out.println(clientDto.getName());
  }
}
