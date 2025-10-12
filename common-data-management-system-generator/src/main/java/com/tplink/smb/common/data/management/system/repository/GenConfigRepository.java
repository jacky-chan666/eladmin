/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.repository;

import com.tplink.smb.common.data.management.system.domain.GenConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface GenConfigRepository extends JpaRepository<GenConfig, Long> {

  /**
   * 查询表配置
   *
   * @param tableName 表名
   * @return /
   */
  GenConfig findByTableName(String tableName);
}
