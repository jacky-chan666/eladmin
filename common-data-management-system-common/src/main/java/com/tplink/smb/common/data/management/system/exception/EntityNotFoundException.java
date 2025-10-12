/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.exception;

import org.springframework.util.StringUtils;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(Class clazz, String field, String val) {
    super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), field, val));
  }

  private static String generateMessage(String entity, String field, String val) {
    return StringUtils.capitalize(entity) + " with " + field + " " + val + " does not exist";
  }
}
