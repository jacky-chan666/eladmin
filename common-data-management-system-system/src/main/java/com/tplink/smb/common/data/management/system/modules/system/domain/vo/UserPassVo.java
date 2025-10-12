/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class UserPassVo {

  @ApiModelProperty(value = "旧密码")
  private String oldPass;

  @ApiModelProperty(value = "新密码")
  private String newPass;
}
