/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.tplink.smb.common.data.management.system.annotation.DataPermission;
import com.tplink.smb.common.data.management.system.annotation.Query;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@DataPermission(fieldName = "id")
public class DeptQueryCriteria {

  @ApiModelProperty(value = "名称")
  @Query(type = Query.Type.INNER_LIKE)
  private String name;

  @Query
  @ApiModelProperty(value = "是否启用")
  private Boolean enabled;

  @Query
  @ApiModelProperty(value = "上级部门")
  private Long pid;

  @ApiModelProperty(value = "PID空查询", hidden = true)
  @Query(type = Query.Type.IS_NULL, propName = "pid")
  private Boolean pidIsNull;

  @ApiModelProperty(value = "创建时间")
  @Query(type = Query.Type.BETWEEN)
  private List<Timestamp> createTime;
}
