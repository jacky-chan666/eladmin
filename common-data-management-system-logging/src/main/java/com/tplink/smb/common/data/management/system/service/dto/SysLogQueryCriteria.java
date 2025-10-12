/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service.dto;

import com.tplink.smb.common.data.management.system.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class SysLogQueryCriteria {

  @ApiModelProperty(value = "模糊查询")
  @Query(blurry = "username,description,address,requestIp,method,params")
  private String blurry;

  @Query
  @ApiModelProperty(value = "用户名")
  private String username;

  @Query
  @ApiModelProperty(value = "日志类型")
  private String logType;

  @ApiModelProperty(value = "创建时间")
  @Query(type = Query.Type.BETWEEN)
  private List<Timestamp> createTime;
}
