/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.tplink.smb.common.data.management.system.annotation.Query;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
@NoArgsConstructor
public class JobQueryCriteria {

  @ApiModelProperty(value = "岗位名称")
  @Query(type = Query.Type.INNER_LIKE)
  private String name;

  @Query
  @ApiModelProperty(value = "岗位状态")
  private Boolean enabled;

  @ApiModelProperty(value = "创建时间")
  @Query(type = Query.Type.BETWEEN)
  private List<Timestamp> createTime;
}
