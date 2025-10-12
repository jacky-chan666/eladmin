/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service;

import com.tplink.smb.common.data.management.system.domain.vo.EmailVo;
import com.tplink.smb.common.data.management.system.domain.EmailConfig;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
public interface EmailService {

  /**
   * 更新邮件配置
   *
   * @param emailConfig 邮箱配置
   * @param old /
   * @return /
   * @throws Exception /
   */
  EmailConfig config(EmailConfig emailConfig, EmailConfig old) throws Exception;

  /**
   * 查询配置
   *
   * @return EmailConfig 邮件配置
   */
  EmailConfig find();

  /**
   * 发送邮件
   *
   * @param emailVo 邮件发送的内容
   * @param emailConfig 邮件配置
   */
  void send(EmailVo emailVo, EmailConfig emailConfig);
}
