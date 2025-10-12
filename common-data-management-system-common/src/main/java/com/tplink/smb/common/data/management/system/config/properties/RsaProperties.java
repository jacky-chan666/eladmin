/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@Component
public class RsaProperties {

  public static String privateKey;

  @Value("${rsa.private_key}")
  public void setPrivateKey(String privateKey) {
    RsaProperties.privateKey = privateKey;
  }
}
