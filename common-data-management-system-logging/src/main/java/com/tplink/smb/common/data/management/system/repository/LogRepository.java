/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.repository;

import com.tplink.smb.common.data.management.system.domain.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Repository
public interface LogRepository
    extends JpaRepository<SysLog, Long>, JpaSpecificationExecutor<SysLog> {

  /**
   * 根据日志类型删除信息
   *
   * @param logType 日志类型
   */
  @Modifying
  @Query(value = "delete from sys_log where log_type = ?1", nativeQuery = true)
  void deleteByLogType(String logType);
}
