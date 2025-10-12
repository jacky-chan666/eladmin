/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.websocket;

import lombok.Data;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class SocketMsg {
  private String msg;
  private MsgType msgType;

  public SocketMsg(String msg, MsgType msgType) {
    this.msg = msg;
    this.msgType = msgType;
  }
}
