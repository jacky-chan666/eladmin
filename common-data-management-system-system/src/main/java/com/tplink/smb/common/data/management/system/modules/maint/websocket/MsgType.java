/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.websocket;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public enum MsgType {
  /** 连接 */
  CONNECT,
  /** 关闭 */
  CLOSE,
  /** 信息 */
  INFO,
  /** 错误 */
  ERROR
}
