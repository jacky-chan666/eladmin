/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

  /* 全部的数据权限 */
  ALL("全部", "全部的数据权限"),

  /* 自己部门的数据权限 */
  THIS_LEVEL("本级", "自己部门的数据权限"),

  /* 自定义的数据权限 */
  CUSTOMIZE("自定义", "自定义的数据权限");

  private final String value;
  private final String description;

  public static DataScopeEnum find(String val) {
    for (DataScopeEnum dataScopeEnum : DataScopeEnum.values()) {
      if (dataScopeEnum.getValue().equals(val)) {
        return dataScopeEnum;
      }
    }
    return null;
  }
}
