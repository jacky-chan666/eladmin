/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class DeptSmallDto implements Serializable {

  @ApiModelProperty(value = "ID")
  private Long id;

  @ApiModelProperty(value = "名称")
  private String name;
}
