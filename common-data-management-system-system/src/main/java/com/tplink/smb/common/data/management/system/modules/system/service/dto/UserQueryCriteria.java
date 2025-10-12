/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.modules.system.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.tplink.smb.common.data.management.system.annotation.Query;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class UserQueryCriteria implements Serializable {

  @Query
  @ApiModelProperty(value = "ID")
  private Long id;

  @ApiModelProperty(value = "部门ID集合")
  @Query(propName = "id", type = Query.Type.IN, joinName = "dept")
  private Set<Long> deptIds = new HashSet<>();

  @ApiModelProperty(value = "模糊查询")
  @Query(blurry = "email,username,nickName")
  private String blurry;

  @Query
  @ApiModelProperty(value = "是否启用")
  private Boolean enabled;

  @ApiModelProperty(value = "部门ID")
  private Long deptId;

  @ApiModelProperty(value = "创建时间")
  @Query(type = Query.Type.BETWEEN)
  private List<Timestamp> createTime;
}
