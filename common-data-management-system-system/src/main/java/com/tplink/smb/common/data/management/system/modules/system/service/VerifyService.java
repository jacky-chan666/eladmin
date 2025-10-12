/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service;

import com.tplink.smb.common.data.management.system.domain.vo.EmailVo;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface VerifyService {

  /**
   * 发送验证码
   *
   * @param email /
   * @param key /
   * @return /
   */
  EmailVo sendEmail(String email, String key);

  /**
   * 验证
   *
   * @param code /
   * @param key /
   */
  void validated(String key, String code);
}
