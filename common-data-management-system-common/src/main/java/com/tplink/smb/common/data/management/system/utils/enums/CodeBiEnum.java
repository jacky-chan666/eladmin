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
public enum CodeBiEnum {

  /* 旧邮箱修改邮箱 */
  ONE(1, "旧邮箱修改邮箱"),

  /* 通过邮箱修改密码 */
  TWO(2, "通过邮箱修改密码");

  private final Integer code;
  private final String description;

  public static CodeBiEnum find(Integer code) {
    for (CodeBiEnum value : CodeBiEnum.values()) {
      if (value.getCode().equals(code)) {
        return value;
      }
    }
    return null;
  }
}
