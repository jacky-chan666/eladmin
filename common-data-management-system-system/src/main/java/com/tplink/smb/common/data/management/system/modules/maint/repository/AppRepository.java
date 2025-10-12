/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.repository;

import com.tplink.smb.common.data.management.system.modules.maint.domain.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface AppRepository extends JpaRepository<App, Long>, JpaSpecificationExecutor<App> {}
