/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@AllArgsConstructor
public enum RequestMethodEnum {

  /** 搜寻 @AnonymousGetMapping */
  GET("GET"),

  /** 搜寻 @AnonymousPostMapping */
  POST("POST"),

  /** 搜寻 @AnonymousPutMapping */
  PUT("PUT"),

  /** 搜寻 @AnonymousPatchMapping */
  PATCH("PATCH"),

  /** 搜寻 @AnonymousDeleteMapping */
  DELETE("DELETE"),

  /** 否则就是所有 Request 接口都放行 */
  ALL("All");

  /** Request 类型 */
  private final String type;

  public static RequestMethodEnum find(String type) {
    for (RequestMethodEnum value : RequestMethodEnum.values()) {
      if (value.getType().equals(type)) {
        return value;
      }
    }
    return ALL;
  }
}
