/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.repository;

import com.tplink.smb.common.data.management.system.modules.system.domain.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface DictRepository extends JpaRepository<Dict, Long>, JpaSpecificationExecutor<Dict> {

  /**
   * 删除
   *
   * @param ids /
   */
  void deleteByIdIn(Set<Long> ids);

  /**
   * 查询
   *
   * @param ids /
   * @return /
   */
  List<Dict> findByIdIn(Set<Long> ids);
}
