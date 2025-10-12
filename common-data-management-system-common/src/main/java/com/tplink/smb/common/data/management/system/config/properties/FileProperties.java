/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.config.properties;

import lombok.Data;
import com.tplink.smb.common.data.management.system.utils.ElConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

  /** 文件大小限制 */
  private Long maxSize;

  /** 头像大小限制 */
  private Long avatarMaxSize;

  private ElPath mac;

  private ElPath linux;

  private ElPath windows;

  public ElPath getPath() {
    String os = System.getProperty("os.name");
    if (os.toLowerCase().startsWith(ElConstant.WIN)) {
      return windows;
    } else if (os.toLowerCase().startsWith(ElConstant.MAC)) {
      return mac;
    }
    return linux;
  }

  @Data
  public static class ElPath {

    private String path;

    private String avatar;
  }
}
