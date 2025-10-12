/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.repository;

import com.tplink.smb.common.data.management.system.modules.maint.domain.ServerDeploy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface ServerDeployRepository
    extends JpaRepository<ServerDeploy, Long>, JpaSpecificationExecutor<ServerDeploy> {

  /**
   * 根据IP查询
   *
   * @param ip /
   * @return /
   */
  ServerDeploy findByIp(String ip);
}
