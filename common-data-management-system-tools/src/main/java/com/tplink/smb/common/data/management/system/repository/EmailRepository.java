/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.repository;

import com.tplink.smb.common.data.management.system.domain.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface EmailRepository extends JpaRepository<EmailConfig, Long> {}
