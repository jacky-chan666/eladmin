/*
 * Copyright (c) 2025, TP-Link. All rights reserved.
 */
package com.tplink.smb.common.data.management.system.service.dto;

import com.tplink.smb.common.data.management.system.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Chen Jiayuan
 * @version 1.0
 * @since 2025/9/30
 */
@Data
public class S3StorageQueryCriteria {

  @Query(type = Query.Type.INNER_LIKE)
  @ApiModelProperty(value = "文件名称")
  private String fileName;

  @Query(type = Query.Type.BETWEEN)
  @ApiModelProperty(value = "创建时间")
  private List<Timestamp> createTime;
}
