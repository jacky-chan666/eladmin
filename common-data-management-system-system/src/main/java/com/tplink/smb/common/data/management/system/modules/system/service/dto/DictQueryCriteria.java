/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.tplink.smb.common.data.management.system.annotation.Query;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class DictQueryCriteria {

  @ApiModelProperty(value = "模糊查询")
  @Query(blurry = "name,description")
  private String blurry;
}
