/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.maint.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.tplink.smb.common.data.management.system.annotation.Query;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class DeployHistoryQueryCriteria {

  @ApiModelProperty(value = "模糊查询")
  @Query(blurry = "appName,ip,deployUser")
  private String blurry;

  @Query
  @ApiModelProperty(value = "部署编号")
  private Long deployId;

  @ApiModelProperty(value = "部署时间")
  @Query(type = Query.Type.BETWEEN)
  private List<Timestamp> deployDate;
}
