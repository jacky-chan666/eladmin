/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.utils;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import com.tplink.smb.common.data.management.system.exception.BadRequestException;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public class ValidationUtil {

  /** 验证空 */
  public static void isNull(Object obj, String entity, String parameter, Object value) {
    if (ObjectUtil.isNull(obj)) {
      String msg = entity + " 不存在: " + parameter + " is " + value;
      throw new BadRequestException(msg);
    }
  }

  /** 验证是否为邮箱 */
  public static boolean isEmail(String email) {
    return Validator.isEmail(email);
  }
}
