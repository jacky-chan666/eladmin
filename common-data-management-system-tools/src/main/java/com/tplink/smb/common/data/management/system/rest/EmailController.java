/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.rest;

import com.tplink.smb.common.data.management.system.annotation.Log;
import com.tplink.smb.common.data.management.system.domain.EmailConfig;
import com.tplink.smb.common.data.management.system.domain.vo.EmailVo;
import com.tplink.smb.common.data.management.system.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/email")
@Api(tags = "工具：邮件管理")
public class EmailController {

  private final EmailService emailService;

  @GetMapping
  public ResponseEntity<EmailConfig> queryEmailConfig() {
    return new ResponseEntity<>(emailService.find(), HttpStatus.OK);
  }

  @Log("配置邮件")
  @PutMapping
  @ApiOperation("配置邮件")
  public ResponseEntity<Object> updateEmailConfig(@Validated @RequestBody EmailConfig emailConfig)
      throws Exception {
    emailService.config(emailConfig, emailService.find());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Log("发送邮件")
  @PostMapping
  @ApiOperation("发送邮件")
  public ResponseEntity<Object> sendEmail(@Validated @RequestBody EmailVo emailVo) {
    emailService.send(emailVo, emailService.find());
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
