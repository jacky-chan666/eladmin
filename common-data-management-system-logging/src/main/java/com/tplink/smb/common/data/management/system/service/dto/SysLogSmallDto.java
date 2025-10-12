/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class SysLogSmallDto implements Serializable {

  @ApiModelProperty(value = "描述")
  private String description;

  @ApiModelProperty(value = "请求IP")
  private String requestIp;

  @ApiModelProperty(value = "耗时")
  private Long time;

  @ApiModelProperty(value = "地址")
  private String address;

  @ApiModelProperty(value = "浏览器")
  private String browser;

  @ApiModelProperty(value = "创建时间")
  private Timestamp createTime;
}
