/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.config;

import com.tplink.smb.common.data.management.system.utils.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Component("auditorAware")
public class AuditorConfig implements AuditorAware<String> {

  /**
   * 返回操作员标志信息
   *
   * @return /
   */
  @Override
  public Optional<String> getCurrentAuditor() {
    try {
      // 这里应根据实际业务情况获取具体信息
      return Optional.of(SecurityUtils.getCurrentUsername());
    } catch (Exception ignored) {
    }
    // 用户定时任务，或者无Token调用的情况
    return Optional.of("System");
  }
}
