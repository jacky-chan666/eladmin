/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public class ThrowableUtil {

  /** 获取堆栈信息 */
  public static String getStackTrace(Throwable throwable) {
    StringWriter sw = new StringWriter();
    try (PrintWriter pw = new PrintWriter(sw)) {
      throwable.printStackTrace(pw);
      return sw.toString();
    }
  }
}
