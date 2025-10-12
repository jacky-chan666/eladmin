/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Converter
public class StringMapConverter implements AttributeConverter<Map<String, String>, String> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Map<String, String> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return "{}";
    }
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error converting Map<String, String> to JSON string", e);
    }
  }

  @Override
  public Map<String, String> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.trim().isEmpty() || "null".equals(dbData)) {
      return Collections.emptyMap();
    }
    try {
      return objectMapper.readValue(dbData, new TypeReference<Map<String, String>>() {});
    } catch (IOException e) {
      throw new IllegalArgumentException("Error converting JSON string to Map<String, String>", e);
    }
  }
}
