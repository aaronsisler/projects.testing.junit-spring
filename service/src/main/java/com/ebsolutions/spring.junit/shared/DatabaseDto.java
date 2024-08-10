package com.ebsolutions.spring.junit.shared;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Slf4j
@DynamoDbBean
@SuperBuilder
@NoArgsConstructor
public abstract class DatabaseDto implements Serializable {
    @NonNull
    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String partitionKey;

    @NonNull
    @Getter(onMethod_ = @DynamoDbSortKey)
    private String sortKey;

    @NonNull
    private String name;

    private LocalDateTime createdOn;

    private LocalDateTime lastUpdatedOn;
}