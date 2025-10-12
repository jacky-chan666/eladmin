/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.security.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserDto {

  @ApiModelProperty(value = "Token编号")
  private String uid;

  @ApiModelProperty(value = "用户名")
  private String userName;

  @ApiModelProperty(value = "昵称")
  private String nickName;

  @ApiModelProperty(value = "岗位")
  private String dept;

  @ApiModelProperty(value = "浏览器")
  private String browser;

  @ApiModelProperty(value = "IP")
  private String ip;

  @ApiModelProperty(value = "地址")
  private String address;

  @ApiModelProperty(value = "token")
  private String key;

  @ApiModelProperty(value = "登录时间")
  private Date loginTime;
}
