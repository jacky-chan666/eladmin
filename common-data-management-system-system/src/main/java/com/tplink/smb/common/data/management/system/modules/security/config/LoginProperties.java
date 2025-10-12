/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "login")
public class LoginProperties {

  public static final String cacheKey = "user-login-cache:";

  /** 账号单用户 登录 */
  private boolean singleLogin = false;
}
