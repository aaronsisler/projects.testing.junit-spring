package com.ebsolutions.spring.junit.util;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;

@Component
public class ComparisonUtil {

  public Map<String, JsonNode> compare(@NotNull JsonNode baseNode,
                                       @NotNull JsonNode comparedNode,
                                       List<String> ignoredFields) {
    Map<String, JsonNode> baseNodeMap = new HashMap<>();
    Map<String, JsonNode> comparedNodeMap = new HashMap<>();

    // Can we check on adding the same fieldName to a single HashMap and see if it blows up?
    Iterator<String> baseNodeIterator = baseNode.fieldNames();
    baseNodeIterator.forEachRemaining(
        fieldName -> baseNodeMap.put(fieldName, baseNode.get(fieldName)));

    Iterator<String> comparedNodeIterator = comparedNode.fieldNames();
    comparedNodeIterator.forEachRemaining(
        fieldName -> comparedNodeMap.put(fieldName, comparedNode.get(fieldName)));

    // Remove fields from maps that are to be ignored
    ignoredFields.forEach(baseNodeMap::remove);
    ignoredFields.forEach(comparedNodeMap::remove);

    // Keys that are in the base that aren't in the compared
    Set<String> extraKeysInBaseNode =
        baseNodeMap.keySet().stream()
            .filter(baseKey -> !comparedNodeMap.containsKey(baseKey))
            .collect(Collectors.toSet());

    Set<String> extraKeysInComparedNode =
        comparedNodeMap.keySet().stream()
            .filter(comparedKey -> !baseNodeMap.containsKey(comparedKey))
            .collect(Collectors.toSet());

    // Beginning of any error checks
    Map<String, JsonNode> returnedErrorMap = new HashMap<>();

    if (!extraKeysInBaseNode.isEmpty()) {
      extraKeysInComparedNode
          .forEach(key -> returnedErrorMap.put(key, baseNodeMap.get(key)));
      return returnedErrorMap;
    }

    if (!extraKeysInComparedNode.isEmpty()) {
      extraKeysInComparedNode
          .forEach(key -> returnedErrorMap.put(key, comparedNodeMap.get(key)));
      return returnedErrorMap;
    }

    baseNodeMap.keySet().forEach(fieldName ->
        nodeValueMatches(baseNodeMap.get(fieldName), comparedNodeMap.get(fieldName))
    );

    return returnedErrorMap;
  }

  private void nodeValueMatches(JsonNode baseNode, JsonNode compareNode) {
    if (baseNode.isTextual() && compareNode.isTextual()) {
      Assertions.assertEquals(baseNode.textValue(), compareNode.textValue());
    }
  }

  public Map<String, JsonNode> compare(@NotNull JsonNode baseNode,
                                       @NotNull JsonNode comparedNode) {
    return this.compare(baseNode, comparedNode, new ArrayList<>());
  }
}
