package com.ebsolutions.spring.junit.client;

import com.ebsolutions.spring.junit.config.DatabaseConfig;
import com.ebsolutions.spring.junit.model.Client;
import com.ebsolutions.spring.junit.shared.SortKeyType;
import com.ebsolutions.spring.junit.shared.exception.DataProcessingException;
import com.ebsolutions.spring.junit.shared.util.UniqueIdGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class ClientDao {
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DatabaseConfig databaseConfig;

    public List<Client> readAll() throws DataProcessingException {
        try {
            DynamoDbTable<ClientDto> clientTable = dynamoDbEnhancedClient
                    .table(databaseConfig.getTableName(), TableSchema.fromBean(ClientDto.class));

            List<ClientDto> clientDtos = clientTable
                    .query(
                            QueryConditional.keyEqualTo(
                                    Key
                                            .builder()
                                            .partitionValue(SortKeyType.CLIENT.name())
                                            .build()
                            )
                    )
                    .items()
                    .stream()
                    .toList();

            return clientDtos.stream()
                    .map(clientDto ->
                            Client.builder()
                                    .clientId(StringUtils.remove(clientDto.getSortKey(), SortKeyType.CLIENT.name()))
                                    .name(clientDto.getName())
                                    .createdOn(clientDto.getCreatedOn())
                                    .lastUpdatedOn(clientDto.getLastUpdatedOn())
                                    .build()
                    ).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        }
    }

    public List<Client> create(List<Client> clients) {
        try {
            LocalDateTime now = LocalDateTime.now();

            List<ClientDto> clientDtos = new ArrayList<>();

            clients.forEach(client ->
                    clientDtos.add(ClientDto.builder()
                            .partitionKey(SortKeyType.CLIENT.name())
                            .sortKey(SortKeyType.CLIENT + UniqueIdGenerator.generate())
                            .name(client.getName())
                            .createdOn(now)
                            .lastUpdatedOn(now)
                            .build())
            );

            DynamoDbTable<ClientDto> clientTable = dynamoDbEnhancedClient
                    .table(databaseConfig.getTableName(), TableSchema.fromBean(ClientDto.class));

            WriteBatch.Builder<ClientDto> writeBatchBuilder = WriteBatch.builder(ClientDto.class)
                    .mappedTableResource(clientTable);

            clientDtos.forEach(writeBatchBuilder::addPutItem);

            WriteBatch writeBatch = writeBatchBuilder.build();

            BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest
                    .builder()
                    .addWriteBatch(writeBatch)
                    .build();

            BatchWriteResult batchWriteResult = this.dynamoDbEnhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            if (!batchWriteResult.unprocessedPutItemsForTable(clientTable).isEmpty()) {
                batchWriteResult.unprocessedPutItemsForTable(clientTable).forEach(key ->
                        log.info(key.toString()));
            }

            return clientDtos.stream().map(clientDto ->
                    Client.builder()
                            .clientId(StringUtils.remove(clientDto.getSortKey(), SortKeyType.CLIENT.name()))
                            .name(clientDto.getName())
                            .createdOn(clientDto.getCreatedOn())
                            .lastUpdatedOn(clientDto.getLastUpdatedOn())
                            .build()
            ).collect(Collectors.toList());

        } catch (Exception e) {
            throw new DataProcessingException(MessageFormat.format("Error in {0}: {1}", this.getClass().getName(), e.getMessage()));
        }
    }
}
