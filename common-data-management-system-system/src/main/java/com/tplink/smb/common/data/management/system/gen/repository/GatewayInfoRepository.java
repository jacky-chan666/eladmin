/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.repository;

import com.tplink.smb.common.data.management.system.gen.domain.GatewayInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface GatewayInfoRepository
    extends JpaRepository<GatewayInfo, Integer>, JpaSpecificationExecutor<GatewayInfo> {
  /**
   * 根据 Model 查询
   *
   * @param model /
   * @return /
   */
  GatewayInfo findByModel(String model);

  List<GatewayInfo> findByStatus(Integer i);

  @Query(
      "SELECT d FROM GatewayInfo d WHERE d.status = 1 AND (d.model LIKE %:keyword% OR d.type LIKE %:keyword%)")
  List<GatewayInfo> findActiveDevicesByKeyword(@Param("keyword") String keyword);
}
