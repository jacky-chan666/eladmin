/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.exception.handler;

import lombok.Data;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class ApiError {

  private Integer status = 400;
  private Long timestamp;
  private String message;

  private ApiError() {
    timestamp = System.currentTimeMillis();
  }

  public static ApiError error(String message) {
    ApiError apiError = new ApiError();
    apiError.setMessage(message);
    return apiError;
  }

  public static ApiError error(Integer status, String message) {
    ApiError apiError = new ApiError();
    apiError.setStatus(status);
    apiError.setMessage(message);
    return apiError;
  }
}
