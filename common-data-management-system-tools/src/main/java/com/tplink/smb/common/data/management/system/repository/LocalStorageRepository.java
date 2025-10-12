/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.repository;

import com.tplink.smb.common.data.management.system.domain.LocalStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface LocalStorageRepository
    extends JpaRepository<LocalStorage, Long>, JpaSpecificationExecutor<LocalStorage> {}
