/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.gen.service.dto;

import com.tplink.smb.common.data.management.system.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class DeviceInfoQueryCriteria {

  /** 模糊 */
  @Query(type = Query.Type.INNER_LIKE)
  @ApiModelProperty(value = "模型类型")
  private String model;

  /** 精确 */
  @Query
  @ApiModelProperty(value = "模型版本")
  private String modelVersion;
}
