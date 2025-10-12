/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.repository;

import com.tplink.smb.common.data.management.system.modules.system.domain.DictDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface DictDetailRepository
    extends JpaRepository<DictDetail, Long>, JpaSpecificationExecutor<DictDetail> {

  /**
   * 根据字典名称查询
   *
   * @param name /
   * @return /
   */
  List<DictDetail> findByDictName(String name);
}
