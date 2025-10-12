/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service;

import java.util.Map;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface MonitorService {

  /**
   * 查询数据分页
   *
   * @return Map<String,Object>
   */
  Map<String, Object> getServers();
}
