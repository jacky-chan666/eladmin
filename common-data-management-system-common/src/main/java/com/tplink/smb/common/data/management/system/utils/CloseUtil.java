/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.utils;

import java.io.Closeable;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public class CloseUtil {

  public static void close(Closeable closeable) {
    if (null != closeable) {
      try {
        closeable.close();
      } catch (Exception e) {
        // 静默关闭
      }
    }
  }

  public static void close(AutoCloseable closeable) {
    if (null != closeable) {
      try {
        closeable.close();
      } catch (Exception e) {
        // 静默关闭
      }
    }
  }
}
