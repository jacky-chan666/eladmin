/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.repository;

import com.tplink.smb.common.data.management.system.domain.ColumnInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface ColumnInfoRepository extends JpaRepository<ColumnInfo, Long> {

  /**
   * 查询表信息
   *
   * @param tableName 表格名
   * @return 表信息
   */
  List<ColumnInfo> findByTableNameOrderByIdAsc(String tableName);
}
