/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.quartz.repository;

import com.tplink.smb.common.data.management.system.modules.quartz.domain.QuartzJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface QuartzJobRepository
    extends JpaRepository<QuartzJob, Long>, JpaSpecificationExecutor<QuartzJob> {

  /**
   * 查询启用的任务
   *
   * @return List
   */
  List<QuartzJob> findByIsPauseIsFalse();
}
