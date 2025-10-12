/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.repository;

import com.tplink.smb.common.data.management.system.gen.domain.DeviceInfo;
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
public interface DeviceInfoRepository
    extends JpaRepository<DeviceInfo, Integer>, JpaSpecificationExecutor<DeviceInfo> {
  boolean existsByModel(String model);

  DeviceInfo findByModel(String model);

  List<DeviceInfo> findByStatus(Integer status);

  @Query("SELECT d FROM DeviceInfo d WHERE d.status = 1 AND d.model LIKE %:keyword% ")
  List<DeviceInfo> findActiveDevicesByKeyword(@Param("keyword") String keyword);
}
