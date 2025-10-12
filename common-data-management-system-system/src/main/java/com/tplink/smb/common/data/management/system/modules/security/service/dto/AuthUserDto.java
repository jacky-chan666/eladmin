/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.security.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Getter
@Setter
public class AuthUserDto {

  @NotBlank
  @ApiModelProperty(value = "用户名")
  private String username;

  @NotBlank
  @ApiModelProperty(value = "密码")
  private String password;

  @ApiModelProperty(value = "验证码")
  private String code;

  @ApiModelProperty(value = "验证码的key")
  private String uuid = "";
}
