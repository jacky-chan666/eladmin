/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.repository;

import com.tplink.smb.common.data.management.system.gen.domain.ApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface ApplicationFormRepository
    extends JpaRepository<ApplicationForm, Integer>, JpaSpecificationExecutor<ApplicationForm> {
  /**
   * 根据 ApplicantId 查询
   *
   * @param uuid /
   * @return /
   */
  ApplicationForm findByUuid(String uuid);

  /**
   * 根据 ApplicationDataId 查询
   *
   * @param application_data_id /
   * @return /
   */
  ApplicationForm findByApplicationDataId(Integer application_data_id);
}
