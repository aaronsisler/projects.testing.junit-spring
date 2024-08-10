package com.ebsolutions.spring.junit.client;

import com.ebsolutions.spring.junit.shared.DatabaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@Slf4j
@DynamoDbBean
@SuperBuilder
@AllArgsConstructor
public class ClientDto extends DatabaseDto {
}
