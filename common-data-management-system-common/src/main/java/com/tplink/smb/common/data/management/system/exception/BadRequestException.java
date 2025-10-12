/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
public class BadRequestException extends RuntimeException {

  private Integer status = BAD_REQUEST.value();

  public BadRequestException(String msg) {
    super(msg);
  }

  public BadRequestException(HttpStatus status, String msg) {
    super(msg);
    this.status = status.value();
  }
}
