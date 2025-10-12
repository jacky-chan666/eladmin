/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.repository;

import com.tplink.smb.common.data.management.system.gen.domain.ImageInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImageInfoRepository
    extends JpaRepository<ImageInfoPO, Integer>, JpaSpecificationExecutor<ImageInfoPO> {
  ImageInfoPO findByModelAndModelVersion(String model, String modelVersion);

  ImageInfoPO findByCompoundModel(String compoundModel);
}
