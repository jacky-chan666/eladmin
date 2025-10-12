/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.rest;

import com.tplink.smb.common.data.management.system.modules.system.service.VerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.tplink.smb.common.data.management.system.domain.vo.EmailVo;
import com.tplink.smb.common.data.management.system.service.EmailService;
import com.tplink.smb.common.data.management.system.utils.enums.CodeBiEnum;
import com.tplink.smb.common.data.management.system.utils.enums.CodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
@Api(tags = "系统：验证码管理")
public class VerifyController {

  private final VerifyService verificationCodeService;
  private final EmailService emailService;

  @PostMapping(value = "/resetEmail")
  @ApiOperation("重置邮箱，发送验证码")
  public ResponseEntity<Object> resetEmail(@RequestParam String email) {
    EmailVo emailVo =
        verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
    emailService.send(emailVo, emailService.find());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(value = "/email/resetPass")
  @ApiOperation("重置密码，发送验证码")
  public ResponseEntity<Object> resetPass(@RequestParam String email) {
    EmailVo emailVo =
        verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
    emailService.send(emailVo, emailService.find());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/validated")
  @ApiOperation("验证码验证")
  public ResponseEntity<Object> validated(
      @RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi) {
    CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
    switch (Objects.requireNonNull(biEnum)) {
      case ONE:
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
        break;
      case TWO:
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
        break;
      default:
        break;
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
