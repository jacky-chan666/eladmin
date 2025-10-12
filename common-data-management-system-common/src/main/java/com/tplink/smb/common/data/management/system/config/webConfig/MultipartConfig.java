/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.config.webConfig;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Configuration
public class MultipartConfig {

  /** 文件上传临时路径 */
  @Bean
  MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    String location = System.getProperty("user.home") + "/.eladmin/file/tmp";
    File tmpFile = new File(location);
    if (!tmpFile.exists()) {
      if (!tmpFile.mkdirs()) {
        System.out.println("create was not successful.");
      }
    }
    factory.setLocation(location);
    return factory.createMultipartConfig();
  }
}
