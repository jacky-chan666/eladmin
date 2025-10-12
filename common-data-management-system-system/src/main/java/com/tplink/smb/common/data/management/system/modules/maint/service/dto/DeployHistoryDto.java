/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class DeployHistoryDto implements Serializable {

  @ApiModelProperty(value = "ID")
  private String id;

  @ApiModelProperty(value = "应用名称")
  private String appName;

  @ApiModelProperty(value = "部署IP")
  private String ip;

  @ApiModelProperty(value = "部署时间")
  private Timestamp deployDate;

  @ApiModelProperty(value = "部署人员")
  private String deployUser;

  @ApiModelProperty(value = "部署编号")
  private Long deployId;
}
