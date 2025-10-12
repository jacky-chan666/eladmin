/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

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
public class MenuQueryCriteria {

  @ApiModelProperty(value = "模糊查询")
  @Query(blurry = "title,component,permission")
  private String blurry;

  @ApiModelProperty(value = "创建时间")
  @Query(type = Query.Type.BETWEEN)
  private List<Timestamp> createTime;

  @ApiModelProperty(value = "PID空查询", hidden = true)
  @Query(type = Query.Type.IS_NULL, propName = "pid")
  private Boolean pidIsNull;

  @Query
  @ApiModelProperty(value = "PID")
  private Long pid;
}
