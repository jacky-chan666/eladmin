/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.repository;

import com.tplink.smb.common.data.management.system.modules.system.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

  /**
   * 根据名称查询
   *
   * @param name 名称
   * @return /
   */
  Job findByName(String name);

  /**
   * 根据Id删除
   *
   * @param ids /
   */
  void deleteAllByIdIn(Set<Long> ids);
}
