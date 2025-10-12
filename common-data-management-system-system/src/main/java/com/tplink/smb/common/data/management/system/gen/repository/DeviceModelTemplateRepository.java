/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.repository;

import com.tplink.smb.common.data.management.system.gen.domain.DeviceModelTemplatePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeviceModelTemplateRepository
    extends JpaRepository<DeviceModelTemplatePO, Integer>,
        JpaSpecificationExecutor<DeviceModelTemplatePO> {
  DeviceModelTemplatePO findByModelAndModelVersion(String model, String modelVersion);
}
