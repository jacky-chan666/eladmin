/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Converter
public class GenericJsonConverter implements AttributeConverter<Object, String> {

  // 共享的 ObjectMapper 实例（线程安全）
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /** 将实体字段的对象转换为 JSON 字符串，保存到数据库 */
  @Override
  public String convertToDatabaseColumn(Object attribute) {
    if (attribute == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("无法将对象序列化为 JSON 字符串", e);
    }
  }

  /** 将数据库中的 JSON 字符串还原为 Java 对象 默认解析成 Map<String, Object> 层级结构（适合动态 JSON） */
  @Override
  public Object convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.trim().isEmpty()) {
      return new HashMap<>(); // 返回空 map 避免 NPE，也可返回 null
    }
    try {
      return objectMapper.readValue(dbData, Map.class);
    } catch (IOException e) {
      throw new IllegalArgumentException("无法将 JSON 字符串反序列化为对象", e);
    }
  }
}
