/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service;

import com.tplink.smb.common.data.management.system.domain.GenConfig;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface GenConfigService {

  /**
   * 查询表配置
   *
   * @param tableName 表名
   * @return 表配置
   */
  GenConfig find(String tableName);

  /**
   * 更新表配置
   *
   * @param tableName 表名
   * @param genConfig 表配置
   * @return 表配置
   */
  GenConfig update(String tableName, GenConfig genConfig);
}
