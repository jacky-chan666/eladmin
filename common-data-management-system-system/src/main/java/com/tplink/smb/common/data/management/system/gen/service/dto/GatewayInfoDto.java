/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class GatewayInfoDto implements Serializable {

  @ApiModelProperty(value = "ID")
  private Integer id;

  @ApiModelProperty(value = "sb模型类型")
  private String model;

  @ApiModelProperty(value = "sb模型版本")
  private String modelVersion;

  @ApiModelProperty(value = "sb设备名称")
  private String sbname;

  @ApiModelProperty(value = "sb设备类型")
  private String type;

  @ApiModelProperty(value = "制造商")
  private String manufacturer;

  @ApiModelProperty(value = "规格参数")
  private String specifications;

  @ApiModelProperty(value = "状态：0-下线，1-上线")
  private Integer status;

  @ApiModelProperty(value = "创建时间")
  private Timestamp createdAt;

  @ApiModelProperty(value = "更新时间")
  private Timestamp updatedAt;
}
