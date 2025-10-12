/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.utils;

import org.apache.commons.configuration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public class ColUtil {
  private static final Logger log = LoggerFactory.getLogger(ColUtil.class);

  /**
   * 转换mysql数据类型为java数据类型
   *
   * @param type 数据库字段类型
   * @return String
   */
  static String cloToJava(String type) {
    Configuration config = getConfig();
    assert config != null;
    return config.getString(type, "unknowType");
  }

  /** 获取配置信息 */
  public static PropertiesConfiguration getConfig() {
    try {
      return new PropertiesConfiguration("gen.properties");
    } catch (ConfigurationException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
