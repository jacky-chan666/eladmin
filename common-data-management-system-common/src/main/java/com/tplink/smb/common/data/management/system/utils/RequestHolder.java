/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public class RequestHolder {

  public static HttpServletRequest getHttpServletRequest() {
    return ((ServletRequestAttributes)
            Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
        .getRequest();
  }
}
